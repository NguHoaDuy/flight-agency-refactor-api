package vn.nhd.flightagency.account.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

public interface JwtService {
    String createJwt(String username, String role);
    String verify(String token) throws JWTVerificationException;
}
