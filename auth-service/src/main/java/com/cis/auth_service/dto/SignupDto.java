package com.cis.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SignupDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Long mobileNumber;
}
