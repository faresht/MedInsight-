package com.medinsight.auth.client;

import com.medinsight.auth.dto.keycloak.KeycloakDTOs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.server-url}")
public interface KeycloakFeignClient {

        // Get Admin Token
        @PostMapping(value = "/realms/master/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        KeycloakDTOs.TokenResponse getAdminToken(@RequestBody Map<String, ?> form);

        // Create User
        @PostMapping(value = "/admin/realms/{realm}/users", consumes = MediaType.APPLICATION_JSON_VALUE)
        void createUser(@RequestHeader("Authorization") String token,
                        @PathVariable("realm") String realm,
                        @RequestBody KeycloakDTOs.UserRepresentation user);

        // Get Users by Username
        @GetMapping(value = "/admin/realms/{realm}/users")
        List<KeycloakDTOs.UserRepresentation> getUsersByUsername(@RequestHeader("Authorization") String token,
                        @PathVariable("realm") String realm,
                        @RequestParam("username") String username,
                        @RequestParam("exact") Boolean exact);

        // Get Role
        @GetMapping(value = "/admin/realms/{realm}/roles/{roleName}")
        KeycloakDTOs.RoleRepresentation getRole(@RequestHeader("Authorization") String token,
                        @PathVariable("realm") String realm,
                        @PathVariable("roleName") String roleName);

        // Assign Role
        @PostMapping(value = "/admin/realms/{realm}/users/{userId}/role-mappings/realm", consumes = MediaType.APPLICATION_JSON_VALUE)
        void assignRole(@RequestHeader("Authorization") String token,
                        @PathVariable("realm") String realm,
                        @PathVariable("userId") String userId,
                        @RequestBody List<KeycloakDTOs.RoleRepresentation> roles);

        // Reset Password
        @PutMapping(value = "/admin/realms/{realm}/users/{userId}/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
        void resetPassword(@RequestHeader("Authorization") String token,
                        @PathVariable("realm") String realm,
                        @PathVariable("userId") String userId,
                        @RequestBody KeycloakDTOs.Credential credential);
}
