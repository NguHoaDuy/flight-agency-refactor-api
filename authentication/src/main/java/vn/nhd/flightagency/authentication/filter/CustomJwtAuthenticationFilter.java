package vn.nhd.flightagency.authentication.filter;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private AuthenticationConverter authenticationConverter;
    //private final AuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    //private final AuthenticationEntryPoint authenticationEntryPoint = new JwtAuthenticationEntryPoint();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final boolean debug = this.logger.isDebugEnabled();
        try {
            UsernamePasswordAuthenticationToken authRequest = (UsernamePasswordAuthenticationToken) authenticationConverter.convert(request);
            if (authRequest == null) {
                this.logger.trace("Did not process authentication request since failed to find "
                        + "token in Bearer Authorization header");
                chain.doFilter(request, response);
                return;
            }
            String username = authRequest.getName();
            this.logger.trace(LogMessage.format("Found username '%s' in Jwt Authorization header", username));
            if (debug) {
                this.logger
                        .debug("Jwt Authentication Authorization header found for user '"
                                + username + "'");
            }
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        catch (AuthenticationException ex) {
            this.logger.debug("Failed to process authentication request", ex);
            SecurityContextHolder.clearContext();
                this.authenticationEntryPoint.commence(request, response, ex);
            return;
        }
        chain.doFilter(request, response);
    }
}
