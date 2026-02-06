package com.medinsight.auth.dto;

import com.medinsight.auth.enums.UserType;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserType userType;
    private boolean enabled = true;
}
