package com.medinsight.auth.dto.keycloak;

import java.util.List;
import java.util.Map;

public class KeycloakDTOs {

    public record UserRepresentation(
            String id,
            String username,
            String email,
            String firstName,
            String lastName,
            Boolean enabled,
            Boolean emailVerified,
            List<Credential> credentials,
            Map<String, List<String>> attributes) {
    }

    public record Credential(
            String type,
            String value,
            Boolean temporary) {
    }

    public record TokenResponse(
            String access_token,
            Integer expires_in,
            String refresh_token,
            String token_type) {
    }

    public record RoleRepresentation(
            String id,
            String name,
            String description,
            Boolean composite,
            Boolean clientRole,
            String containerId) {
    }
}
