package com.cis.auth_service.service;

import com.cis.auth_service.dto.*;

import java.util.List;

public interface AuthService {
    SignupResponse registerUser(SignupDto signupDto);
    List<UserDto> getAllUsers();
    SigninResponseDto loginUser(SigninDto signinDto);
}
