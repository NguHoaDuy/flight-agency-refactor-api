package vn.nhd.flightagency.email.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import vn.nhd.flightagency.email.config.EmailConfig;
import vn.nhd.flightagency.email.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public void sendVerificationCode(String userMail, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> model = new HashMap<>();

        model.put("redirectUrl", EmailConfig.VERIFY_CODE_URI + verificationCode);
        model.put("user", userMail.replace("@gmail.com", ""));
        context.setVariables(model);
        String html = springTemplateEngine.process("verify-account", context);
        helper.setTo(userMail);
        helper.setText(html, true);
        helper.setSubject("Account Activation at NHD Flight Agency");
        helper.setFrom("nhd.flight.agency", "NHD Flight Agency");
        javaMailSender.send(message);
    }

    @Override
    public void sendResetPasswordCode(String userMail, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> model = new HashMap<>();

        model.put("redirectUrl", EmailConfig.RESET_PASSWORD_URI + verificationCode);
        model.put("user", userMail.replace("@gmail.com", ""));
        context.setVariables(model);
        String html = springTemplateEngine.process("reset-password", context);
        helper.setTo(userMail);
        helper.setText(html, true);
        helper.setSubject("Reset password at NHD Flight Agency");
        helper.setFrom("nhd.flight.agency", "NHD Flight Agency");
        javaMailSender.send(message);
    }
}
