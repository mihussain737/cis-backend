package com.cis.billing_service.config;

import com.cis.billing_service.security.AuthEntryPointImpl;
import com.cis.billing_service.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthEntryPointImpl authEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader=request.getHeader("Authorization");
        if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String jwtToken=authorizationHeader.substring(7);
        try{
            Claims claims=jwtUtil.extractClaims(jwtToken);

            String username=claims.getSubject();
            String role=claims.get("role").toString();
            logger.debug("USERNAME: {}"+username);
            logger.debug("ROLE: {}"+role);

            if(role!=null && !role.startsWith("ROLE_")){
                role="ROLE_"+role;
            }
            List<SimpleGrantedAuthority> authorities=role==null?List.of():List.of(new SimpleGrantedAuthority(role));
            logger.debug("AUTHORITY: {}"+authorities);

            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            authEntryPoint.commence(request,response, new CredentialsExpiredException( "Token has expired. Please login again."));
            return;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("JWT validation faile: {}"+e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
