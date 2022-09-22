package com.twoauth.test.security.jwt;

import com.twoauth.test.security.CustomUserDetails;
import com.twoauth.test.service.CustomUserDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public abstract class JwtFilterAbstract extends GenericFilterBean {

    JwtProvider jwtProvider;
    CustomUserDetailsService customUserDetailsService;

    public static final String AUTHORIZATION = "Authorization";

    protected void doFilterInternal(ServletRequest servletRequest, TokenType tokenType) {
        Optional<String> token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token.isPresent() && jwtProvider.isValidToken(token.get())) {
            log.info("Do filter for {} token", tokenType.getType());
            TokenType level = jwtProvider.getTokenLevel(token.get());
            if (level.equals(tokenType)) {
                String userLogin = jwtProvider.getLoginFromToken(token.get());
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
                UsernamePasswordAuthenticationToken auth = getUsernamePasswordAuthenticationToken(customUserDetails);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
    }

    protected Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return Optional.of(bearer.substring(7));
        }
        return Optional.empty();
    }

    protected abstract UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(CustomUserDetails customUserDetails);
}
