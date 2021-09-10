package vn.nhd.flightagency.email.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendVerificationCode(String userMail, String verificationCode) throws MessagingException, UnsupportedEncodingException;

    void sendResetPasswordCode(String userMail, String verificationCode) throws MessagingException, UnsupportedEncodingException;
}
