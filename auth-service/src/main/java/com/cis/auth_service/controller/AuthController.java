package com.cis.auth_service.controller;

import com.cis.auth_service.dto.SignupDto;
import com.cis.auth_service.dto.SignupResponse;
import com.cis.auth_service.dto.UserDto;
import com.cis.auth_service.entity.User;
import com.cis.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
