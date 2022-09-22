package com.twoauth.test.security.jwt;

import com.twoauth.test.security.CustomUserDetails;
import com.twoauth.test.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends JwtFilterAbstract {

    public JwtAuthFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        super(jwtProvider, customUserDetailsService);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        super.doFilterInternal(servletRequest, TokenType.ACCESS_TOKEN);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    protected UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(CustomUserDetails customUserDetails) {
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

}
