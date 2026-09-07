package com.cis.auth_service.service.impl;

import com.cis.auth_service.dto.RoleDto;
import com.cis.auth_service.dto.SignupDto;
import com.cis.auth_service.dto.SignupResponse;
import com.cis.auth_service.dto.UserDto;
import com.cis.auth_service.entity.Role;
import com.cis.auth_service.entity.User;
import com.cis.auth_service.exception.UserAlreadyExist;
import com.cis.auth_service.repository.RoleRepository;
import com.cis.auth_service.repository.UserRepository;
import com.cis.auth_service.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SignupResponse registerUser(SignupDto signupDto) {
        Optional<User> byUsername = userRepository.findByUsername(signupDto.getUsername());
        if (byUsername.isPresent()) {
            throw new UserAlreadyExist("User already exists");
        }
        if(signupDto.getConfirmPassword()!=null && !signupDto.getPassword().equals(signupDto.getConfirmPassword())){
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        User user = modelMapper.map(signupDto, User.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        Role role = roleRepository.findByRoleName("ROLE_USER");
        user.setRole(role);
        User save = userRepository.save(user);
        SignupResponse signupResponse = modelMapper.map(save, SignupResponse.class);
        signupResponse.setRole(modelMapper.map(save.getRole(), RoleDto.class));
        return signupResponse;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDto.setRole(modelMapper.map(user.getRole(), RoleDto.class));
            return userDto;
        }).toList();
    }
}
