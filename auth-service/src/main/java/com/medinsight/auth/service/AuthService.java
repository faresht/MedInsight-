package com.medinsight.auth.service;

import com.medinsight.common.dto.ApiResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public AuthService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public ApiResponse<String> createUser(UserRepresentation user, String password) {
        UsersResource usersResource = keycloak.realm(realm).users();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {
            return ApiResponse.success("User created successfully");
        } else {
            return ApiResponse.error("Failed to create user: " + response.getStatusInfo(), "USER_CREATION_FAILED");
        }
    }
}
