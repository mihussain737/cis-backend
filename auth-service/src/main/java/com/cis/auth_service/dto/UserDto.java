package com.cis.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private Long mobileNumber;
    private RoleDto role;
}
