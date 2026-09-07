package com.cis.auth_service.controller;

import com.cis.auth_service.dto.SignupDto;
import com.cis.auth_service.dto.SignupResponse;
import com.cis.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody  SignupDto signupDto) {
        SignupResponse signupDto1 = authService.registerUser(signupDto);
        return new ResponseEntity<>(signupDto1, HttpStatus.CREATED);
    }
}
