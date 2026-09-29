package com.cis.billing_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${secret_key}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).
                getPayload();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsernameFromJwt(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRoleFromJwt(String token) {
        return extractClaims(token).get("role").toString();
    }
}
