package vn.nhd.flightagency.authentication.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.nhd.flightagency.account.service.JwtService;

import javax.servlet.http.HttpServletRequest;

@Service
public class CustomJwtAuthenticationConverter implements AuthenticationConverter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer";

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Check authentication header exists
        if (header == null) {
            return null;
        }
        // Check Bearer is defined
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)) {
            return null;
        }

        // Check jwt is defined
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
            throw new BadCredentialsException("Empty basic authentication token");
        }
        // getToken
        String token = header.substring(7);
        try {
            String username = jwtService.verify(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Invalid Bearer authentication token");
        }
    }
}
