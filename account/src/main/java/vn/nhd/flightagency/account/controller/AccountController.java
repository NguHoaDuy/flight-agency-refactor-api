package vn.nhd.flightagency.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.nhd.flightagency.account.config.AccountUri;
import vn.nhd.flightagency.account.model.AccountRequestModel;
import vn.nhd.flightagency.account.model.ResetPasswordRequestModel;
import vn.nhd.flightagency.account.service.AccountService;
import vn.nhd.flightagency.exception.extension.BadCredentialException;
import vn.nhd.flightagency.exception.extension.SocialBadCredentialException;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AccountUri.API_VERSION)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(AccountUri.SIGNUP)
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountRequestModel accountRequestModel, BindingResult bindingResult) throws MessagingException, BadCredentialException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            throw new BadCredentialException(bindingResult);
        }
        accountService.signUp(accountRequestModel);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(AccountUri.VERIFY)
    public ResponseEntity<Void> verify(@RequestParam String code) throws BadCredentialException {
        Map<String, String> info = accountService.verify(code);
        String param = accountService.extractToParam(info);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(AccountService.VERIFY_URI + "?" + param))
                .build();
    }

    @PostMapping(AccountUri.SIGNIN)
    public ResponseEntity<Map<String, String>> signIn(@RequestBody AccountRequestModel accountRequestModel) throws BadCredentialException {
        Map<String, String> info = accountService.signIn(accountRequestModel);
        String param = accountService.extractToParam(info);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url",AccountService.VERIFY_URI + "?" + param);
        return ResponseEntity.ok(urlMap);
    }

    @GetMapping(AccountUri.SOCIAL_LOGIN)
    public ResponseEntity<Void> socialAuthentication(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                       @AuthenticationPrincipal OAuth2User oAuth2User) throws SocialBadCredentialException {
        Map<String, String> info = accountService.socialAuth(authorizedClient, oAuth2User);
        String param = accountService.extractToParam(info);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(AccountService.VERIFY_URI + "?" + param))
                .build();
    }

    @GetMapping(AccountUri.RESET)
    public ResponseEntity<Void> reset(@RequestParam String email) {
        accountService.resetPassword(email);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(AccountUri.VERIFY_RESET)
    public ResponseEntity<Map<String, String>> verifyReset(@RequestBody ResetPasswordRequestModel model) {
        Map<String, String> info = accountService.verifyResetPassword(model);
        return ResponseEntity.ok(info);
    }
}
