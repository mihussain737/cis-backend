package com.cis.payment_service.config;

import com.cis.payment_service.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class PaymentSecurityConfig {

    private final CustomJwtFilter customJwtFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health",
                                "/actuator/info",
                                "/error"
                                ,"/api/payment/test").permitAll()
                        .requestMatchers("/api/payment/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                ).addFilterBefore(
                        customJwtFilter,
                UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public org.modelmapper.ModelMapper modelMapper() {
        return new org.modelmapper.ModelMapper();
    }
}
