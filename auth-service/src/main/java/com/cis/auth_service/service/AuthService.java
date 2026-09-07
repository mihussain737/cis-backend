package com.cis.auth_service.service;

import com.cis.auth_service.dto.SignupDto;
import com.cis.auth_service.dto.SignupResponse;
import com.cis.auth_service.dto.UserDto;

import java.util.List;

public interface AuthService {
    SignupResponse registerUser(SignupDto signupDto);
    List<UserDto> getAllUsers();
}
