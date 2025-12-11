package com.medinsight.auth.controller;

import com.medinsight.auth.service.AuthService;
import com.medinsight.common.dto.ApiResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserRepresentation user,
            @RequestParam String password) {
        return ResponseEntity.ok(authService.createUser(user, password));
    }
}
