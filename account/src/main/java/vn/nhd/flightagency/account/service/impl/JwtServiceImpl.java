package vn.nhd.flightagency.account.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;
import vn.nhd.flightagency.account.service.JwtService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final String secretKey = "NguyenDuy.154@*123abcj1k2h4h5kfjdk1j23hg4hfksdfjh3h5";

    /**
     * Set alg type in header. Using HS256
     * @return Algorithm
     */
    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    private Map<String, Object> setPayload(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "NHD");
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + (60 * 1000 * 5)));
        claims.put("username", username);
        claims.put("role", role);
        return claims;
    }

    public String createJwt(String username, String role) {
        try {
            return JWT.create()
                    .withPayload(setPayload(username, role))
                    .sign(getAlgorithm());
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return "";
        }
    }

    @Override
    public String verify(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer("NHD")
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
