package vn.nhd.flightagency.account.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import vn.nhd.flightagency.account.model.AccountRequestModel;
import vn.nhd.flightagency.account.model.ResetPasswordRequestModel;
import vn.nhd.flightagency.exception.extension.BadCredentialException;
import vn.nhd.flightagency.exception.extension.SocialBadCredentialException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface AccountService {
    String VERIFY_URI = "http://localhost:4200/auth/verify";
    void signUp(AccountRequestModel accountRequestModel) throws MessagingException, UnsupportedEncodingException;
    Map<String, String> verify(String verificationCode) throws BadCredentialException;
    Map<String, String> signIn(AccountRequestModel accountRequestModel) throws BadCredentialException;
    Map<String, String> socialAuth(OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User) throws SocialBadCredentialException, SocialBadCredentialException;
    void resetPassword(String email);
    Map<String, String> verifyResetPassword(ResetPasswordRequestModel model);
    String extractToParam(final Map<String, String> in);
}
