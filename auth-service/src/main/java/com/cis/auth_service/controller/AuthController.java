package com.cis.auth_service.controller;

import com.cis.auth_service.dto.*;
import com.cis.auth_service.security.UserDetailsImpl;
import com.cis.auth_service.security.jwt.JwtUtils;
import com.cis.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @PostMapping("/login")
    public ResponseEntity<SigninResponseDto> loginUser(
            @RequestBody SigninDto signinDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                signinDto.getUsername(),
                                signinDto.getPassword()
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        String jwt =
                jwtUtils.generateTokenFromUsername(userDetails);

        SigninResponseDto response =
                new SigninResponseDto();

        response.setToken(jwt);
        response.setUsername(userDetails.getUsername());
        response.setEmail(userDetails.getEmail());

        response.setRole(
                new RoleDto(
                        userDetails
                                .getAuthorities()
                                .iterator()
                                .next()
                                .getAuthority()
                )
        );

        return ResponseEntity.ok(response);
    }
}
