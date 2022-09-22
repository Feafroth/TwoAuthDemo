package com.twoauth.test.security.mfa;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy MFA SMS Provider
 */
@Component
public class MfaSmsProvider {

    private static final Map<String, String> storage = new HashMap<>();
    private static final String code = "54321";

    public void sendCode(String phone) {
        storage.put(phone, code);
    }

    public Boolean checkCode(String phone, String code) {
        return storage.remove(phone, code);
    }
}
