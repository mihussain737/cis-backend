package com.cis.auth_service.security;

import com.cis.auth_service.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private SimpleGrantedAuthority authority;


    public UserDetailsImpl(
            Long userId,
            String username,
            String email,
            String password,
            SimpleGrantedAuthority authority) {

        this.id = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return username;
    }


    public static UserDetailsImpl build(User user) {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        user.getRole().getRoleName()
                );

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authority
        );
    }
}