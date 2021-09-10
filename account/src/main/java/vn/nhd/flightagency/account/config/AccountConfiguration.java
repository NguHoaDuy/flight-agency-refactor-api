package vn.nhd.flightagency.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.nhd.flightagency.account.service.JwtService;
import vn.nhd.flightagency.account.service.impl.JwtServiceImpl;

@Configuration
public class AccountConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtServiceImpl();
    }

}
