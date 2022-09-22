package com.twoauth.test.security.jwt;

import java.util.Arrays;

public enum TokenType {

    ACCESS_TOKEN("access"), TWO_FA_TOKEN("2fa");

    private final String type;

    TokenType(String type) {
        this.type = type;
    }

    public static TokenType parse(String type) {
        return Arrays.stream(TokenType.values()).filter(t -> t.getType().equals(type)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public String getType() {
        return type;
    }
}
