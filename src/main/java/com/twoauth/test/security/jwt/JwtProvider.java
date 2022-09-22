package com.twoauth.test.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@Log
public class JwtProvider {

    public static final String LEVEL = "level";

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.auth.expire}")
    private String authExpire;
    @Value("${jwt.2fa.expire}")
    private String twoAuthExpire;

    public String generateAccessToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(Integer.parseInt(authExpire)).toInstant(ZoneOffset.UTC));
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .claim(LEVEL, TokenType.ACCESS_TOKEN)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generate2faToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(Integer.parseInt(twoAuthExpire)).toInstant(ZoneOffset.UTC));
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .claim(LEVEL, TokenType.TWO_FA_TOKEN)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            log.severe("Invalid signature");
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public TokenType getTokenLevel(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return TokenType.valueOf((String) claims.get(LEVEL));
    }

}
