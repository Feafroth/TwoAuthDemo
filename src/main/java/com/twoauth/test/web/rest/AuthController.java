package com.twoauth.test.web.rest;

import com.twoauth.test.exception.UserUnauthenticated;
import com.twoauth.test.service.AuthService;
import com.twoauth.test.web.payload.AuthRequest;
import com.twoauth.test.web.payload.AuthResponse;
import com.twoauth.test.web.payload.TwoAuthRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthService authService;

    @PostMapping("/sign-in")
    public AuthResponse auth(@RequestBody AuthRequest request) throws UserUnauthenticated {
        return new AuthResponse(authService.authorizeUserWithPassword(request));
    }

    @PostMapping("/two-auth")
    public AuthResponse twoFactorAuth(@RequestBody TwoAuthRequest request) throws UserUnauthenticated {
        return new AuthResponse(authService.validateCode(request));
    }
}
