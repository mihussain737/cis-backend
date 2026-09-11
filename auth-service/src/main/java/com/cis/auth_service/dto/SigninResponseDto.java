package com.cis.auth_service.dto;

import com.cis.auth_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponseDto {

    private String username;
    private String email;
    private String token;
    private RoleDto role;
}
