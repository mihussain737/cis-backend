package com.cis.payment_service.config;

import com.cis.payment_service.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomJwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
        String authHeader = request.getHeader("Authorization");
        if(authHeader ==null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);

        Claims claims= jwtUtil.extractClaims(jwtToken);
        String role = claims.get("role", String.class);
        String username = claims.getSubject();
        logger.debug("Username: {}"+username);
        logger.debug("JWT role: {}"+role);

        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<SimpleGrantedAuthority> authorities =role == null? List.of() : List.of(new SimpleGrantedAuthority(role));
        logger.debug("Granted authorities: {}"+authorities);

        UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken( username,null, authorities);
        SecurityContextHolder.getContext() .setAuthentication(authentication);
        logger.info("Authentication: {}"+SecurityContextHolder.getContext().getAuthentication());

    } catch (Exception e) {
        logger.error("JWT validation failed: {}"+ e.getMessage());
        SecurityContextHolder.clearContext();
    }
        filterChain.doFilter(request, response);
    }

}
