package com.twoauth.test.service;

import com.twoauth.test.entity.User;
import com.twoauth.test.exception.UserUnauthenticated;
import com.twoauth.test.security.jwt.JwtProvider;
import com.twoauth.test.web.payload.AuthRequest;
import com.twoauth.test.web.payload.TwoAuthRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {

    UserService userService;
    TwoAuthService twoAuthService;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;

    public String authorizeUserWithPassword(AuthRequest request) throws UserUnauthenticated {
        User user = userService.findByLogin(request.getLogin()).orElseThrow(() ->
                new UserUnauthenticated("Cannot authenticate user " + request.getLogin()));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            twoAuthService.requestCode(user.getPhone());
            return jwtProvider.generate2faToken(user.getLogin());
        }
        throw new UserUnauthenticated("Cannot authenticate user " + request.getLogin());
    }

    public String validateCode(TwoAuthRequest request) throws UserUnauthenticated {
        User user = twoAuthService.validateCode(request.getCode())
                .orElseThrow(() -> new UserUnauthenticated("Cannot authenticate user: two-factor authentication failed"));
        return jwtProvider.generateAccessToken(user.getLogin());
    }
}
