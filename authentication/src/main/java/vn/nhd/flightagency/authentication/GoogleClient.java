package vn.nhd.flightagency.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;


public class GoogleClient implements ClientRegistrationRepository {


    @Value("${spring.security.oauth2.client.registration.google-login.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google-login.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google-login.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.registration.google-login.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.registration.google-login.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.google-login.authorization-grant-type}")
    private AuthorizationGrantType grantType;

    @Value("${spring.security.oauth2.client.registration.google-login.redirect-uri}")
    private String redirectUri;

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("google-login");
        return builder
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .redirectUri(redirectUri)
                .scope(scope)
                .authorizationGrantType(grantType)
                .build();
    }
}
