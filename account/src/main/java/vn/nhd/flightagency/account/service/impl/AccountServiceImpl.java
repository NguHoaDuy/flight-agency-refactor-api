package vn.nhd.flightagency.account.service.impl;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.domain.Provider;
import vn.nhd.flightagency.account.domain.User;
import vn.nhd.flightagency.account.model.AccountRequestModel;
import vn.nhd.flightagency.account.model.ResetPasswordRequestModel;
import vn.nhd.flightagency.account.repository.AccountRepository;
import vn.nhd.flightagency.account.repository.UserRepository;
import vn.nhd.flightagency.account.service.AccountMapperService;
import vn.nhd.flightagency.account.service.AccountService;
import vn.nhd.flightagency.account.service.JwtService;
import vn.nhd.flightagency.email.service.EmailService;
import vn.nhd.flightagency.exception.extension.BadCredentialException;
import vn.nhd.flightagency.exception.extension.SocialBadCredentialException;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public void signUp(AccountRequestModel accountRequestModel) throws MessagingException, UnsupportedEncodingException {
        String verificationCode = makeRandomString();
        Account account = AccountMapperService.INSTANCE.accountMapper(accountRequestModel);
        account.setPassword(passwordEncoder.encode(accountRequestModel.getPassword()));
        account.setVerificationCode(verificationCode);
        Account account1 =  accountRepository.save(account);
        addUser(account1);
        emailService.sendVerificationCode(account.getEmail(), verificationCode);
    }


    @Override
    public Map<String, String> verify(String verificationCode) throws BadCredentialException {
        Optional<Account> account = accountRepository.findAccountByVerificationCode(verificationCode);
        Account account1 = account.orElse(null);
        if (account1 != null) {
            account1.setVerificationCode(null);
            account1.setEnabled(true);
            accountRepository.save(account1);
            return getAccountInfo(account1, false);
        }
        // should throw exception here
        throw new BadCredentialException("Verification code is expired.");
    }

    @Override
    public Map<String, String> signIn(AccountRequestModel accountRequestModel) throws BadCredentialException {
        // Check whether the account is enabled
        Optional<Account> optionalAccount = accountRepository.findAccountByUsernameAndProvider(accountRequestModel.getUsername(), Provider.LOCAL);
        Account account = optionalAccount.orElse(null);
        if (account != null) {
            if(passwordEncoder.matches(accountRequestModel.getPassword(), account.getPassword())) {
                return getAccountInfo(account, false);
            }
        }
        // should Bad credential throw exception
        throw new BadCredentialException("Bad Credential");
    }

    @Transactional
    @Override
    public Map<String, String> socialAuth(OAuth2AuthorizedClient authorizedClient, OAuth2User oAuth2User) throws SocialBadCredentialException {
        Provider provider = Provider.getProvider(authorizedClient.getClientRegistration().getClientName());
        // check username
        Optional<Account> account = accountRepository.findAccountByUsername(oAuth2User.getName());
        Account account1 = account.orElse(null);
        if (account1 == null) {
            // check email
            account = accountRepository.findAccountByEmail(oAuth2User.getAttribute("email"));
            account1 = account.orElse(null);
            if (account1 == null) {
                // sign up then sign in
                return signInOAuth(signUpOAuth(oAuth2User, provider));
            }
            // throw already sign up exception here
            throw new SocialBadCredentialException("Account is already signed up.");
        }
        // signIn
        return signInOAuth(account1);
    }

    @Override
    public void resetPassword(String email) {
        Optional<Account> account = accountRepository.findAccountByEmail(email);
        account.ifPresent(acc -> {
            String verificationCode = makeRandomString();
            acc.setResetPasswordCode(verificationCode);
            accountRepository.save(acc);
            try {
                emailService.sendResetPasswordCode(acc.getEmail(), verificationCode);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, String> verifyResetPassword(ResetPasswordRequestModel model) {
        Map<String, String> result = new HashMap<>();
        Optional<Account> account = accountRepository.findAccountByResetPasswordCode(model.getCode());
        Account account1 = account.orElse(null);
        if (account1 != null) {
            account1.setResetPasswordCode(null);
            account1.setPassword(passwordEncoder.encode(model.getPassword()));
            accountRepository.save(account1);
            result.put("reset", "success");
            return result;
        }
        // should throw exception here
        result.put("reset", "fail");
        return result;
    }

    @Override
    public String extractToParam(final Map<String, String> in) {
        Set<String> keySet = in.keySet();
        StringBuilder param = new StringBuilder();
        for (String key : keySet) {
            param.append(key).append("=").append(in.get(key)).append("&");
        }
        param.deleteCharAt(param.toString().length() - 1);
        return param.toString();
    }

    private Map<String, String> signInOAuth(Account account) {
        return getAccountInfo(account, true);
    }
    private Account signUpOAuth(OAuth2User oAuth2User, Provider provider) {
        Account newAccount = Account.builder()
                .username(oAuth2User.getName())
                .email(oAuth2User.getAttribute("email"))
                .provider(provider)
                .enabled(true)
                .verificationCode(null)
                .role("ROLE_USER")
                .build();
        Account signedInAccount = accountRepository.save(newAccount);
        addUser(signedInAccount, oAuth2User);
        return signedInAccount;
    }

    private String makeRandomString() {
        return RandomString.make(64);
    }

    private Map<String, String> getAccountInfo(Account account, boolean isSocial) {
        Map<String, String> info = new TreeMap<>();
        info.put("_t", createJwt(account.getUsername(), account.getRole()));
        info.put("_id", account.getId().toString());
        info.put("_r", account.getRole());
        info.put("_en", account.getEnabled().toString());
        if (isSocial)
            info.put("_n", getNameFromEmail(account.getEmail()));
        else
            info.put("_n", account.getUsername());
        return info;
    }


    private String createJwt(String value, String role) {
        return jwtService.createJwt(value, role);
    }

    private void addUser(Account account) {
        assert account != null;
        addUser(account, null);
    }

    private void addUser(Account account, OAuth2User oAuth2User) {
        final User user = User.builder()
                .account(account)
                .build();
        if(oAuth2User != null) {
            user.setName(oAuth2User.getAttribute("name"));
        } else {
            user.setName(getNameFromEmail(account.getUsername()));
        }
        userRepository.save(user);
    }

    private String getNameFromEmail(String email) {
        Pattern pattern = Pattern.compile("^(([-\\w.]+)[a-zA-Z\\d])@(\\w+\\.)+(\\w+)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }
}
