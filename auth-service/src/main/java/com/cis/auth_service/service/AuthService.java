package com.cis.auth_service.service;

import com.cis.auth_service.dto.SignupDto;
import com.cis.auth_service.dto.SignupResponse;

public interface AuthService {
    SignupResponse registerUser(SignupDto signupDto);
}
