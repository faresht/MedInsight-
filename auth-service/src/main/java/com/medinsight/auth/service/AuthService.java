package com.medinsight.auth.service;

import com.medinsight.auth.client.KeycloakFeignClient;
import com.medinsight.auth.dto.RegisterRequest;
import com.medinsight.auth.dto.keycloak.KeycloakDTOs;
import com.medinsight.auth.entity.Role;
import com.medinsight.auth.entity.User;
import com.medinsight.auth.repository.RoleRepository;
import com.medinsight.auth.repository.UserRepository;
import com.medinsight.common.dto.ApiResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final KeycloakFeignClient keycloakClient;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String clientId;

    @Value("${keycloak.admin-username}")
    private String username;

    @Value("${keycloak.admin-password}")
    private String password;

    public AuthService(KeycloakFeignClient keycloakClient, UserRepository userRepository,
            RoleRepository roleRepository) {
        this.keycloakClient = keycloakClient;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private String getAdminAccessToken() {
        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "password");
        form.put("client_id", clientId);
        form.put("username", username);
        form.put("password", password);
        return "Bearer " + keycloakClient.getAdminToken(form).access_token();
    }

    public ApiResponse<String> createUser(RegisterRequest request) {
        try {
            String token = getAdminAccessToken();

            // 1. Create Keycloak User (No credentials initially)
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("phoneNumber", Collections.singletonList(request.getPhoneNumber()));
            attributes.put("userType", Collections.singletonList(request.getUserType().name()));

            KeycloakDTOs.UserRepresentation kcUser = new KeycloakDTOs.UserRepresentation(
                    null,
                    request.getUsername(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    true,
                    true,
                    null, // No credentials during creation to avoid NPE in Keycloak
                    attributes);

            keycloakClient.createUser(token, realm, kcUser);

            // 2. Fetch Created User ID
            List<KeycloakDTOs.UserRepresentation> users = keycloakClient.getUsersByUsername(token, realm,
                    request.getUsername(), true);
            if (users.isEmpty()) {
                return ApiResponse.error("User created in Keycloak but not found immediately.",
                        "USER_NOT_FOUND_AFTER_CREATE");
            }
            String userId = users.get(0).id();

            // 3. Set Password (Reset Password)
            try {
                KeycloakDTOs.Credential credential = new KeycloakDTOs.Credential("password", request.getPassword(),
                        false);
                keycloakClient.resetPassword(token, realm, userId, credential);
            } catch (Exception e) {
                System.err.println("Failed to set password: " + e.getMessage());
                // Should we cleanup? For now, just log.
            }

            // 4. Assign Role in Keycloak
            try {
                String targetRoleName = request.getUserType().name(); // e.g. PATIENT
                KeycloakDTOs.RoleRepresentation role = keycloakClient.getRole(token, realm, targetRoleName);
                if (role != null) {
                    keycloakClient.assignRole(token, realm, userId, Collections.singletonList(role));
                }
            } catch (Exception e) {
                System.err.println("Failed to assign Keycloak role: " + e.getMessage());
            }

            // 5. Create Local User
            try {
                User localUser = new User();
                localUser.setId(UUID.fromString(userId));
                localUser.setKeycloakId(userId);
                localUser.setUsername(request.getUsername());
                localUser.setEmail(request.getEmail());
                localUser.setFirstName(request.getFirstName());
                localUser.setLastName(request.getLastName());
                localUser.setPhoneNumber(request.getPhoneNumber());
                localUser.setUserType(request.getUserType());
                localUser.setPassword(request.getPassword());
                localUser.setEnabled(true);

                // Assign Local Role
                String localRoleName = "ROLE_" + request.getUserType().name();
                Role userRole = roleRepository.findByName(localRoleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(localRoleName);
                            return roleRepository.save(newRole);
                        });
                localUser.setRoles(Collections.singleton(userRole));
                localUser.setAsNew(); // Mark as new entity for Hibernate

                userRepository.save(localUser);

                return ApiResponse.success("User created successfully");

            } catch (Exception e) {
                System.err.println("Failed to save local user: " + e.getMessage());
                return ApiResponse.error("Failed to save user data: " + e.getMessage(), "DB_SAVE_FAILED");
            }

        } catch (FeignException e) {
            System.err.println("Keycloak Error: " + e.contentUTF8());
            return ApiResponse.error("Keycloak error: " + e.status(), "KEYCLOAK_ERROR");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Internal error: " + e.getMessage(), "INTERNAL_ERROR");
        }
    }
}
