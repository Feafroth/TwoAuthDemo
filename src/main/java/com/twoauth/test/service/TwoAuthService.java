package com.twoauth.test.service;

import com.twoauth.test.entity.User;
import com.twoauth.test.exception.UserUnauthenticated;
import com.twoauth.test.security.mfa.MfaSmsProvider;
import com.twoauth.test.security.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TwoAuthService {

    MfaSmsProvider MFASMSProvider;
    UserService userService;

    public void requestCode(String phone) {
        MFASMSProvider.sendCode(phone);
    }

    public Optional<User> validateCode(String code) throws UserUnauthenticated {
        User user = userService.findByLogin(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin())
                .orElseThrow(() -> new UserUnauthenticated("Cannot authenticate user: user not found"));
        return MFASMSProvider.checkCode(user.getPhone(), code) ? Optional.of(user) : Optional.empty();
    }
}
