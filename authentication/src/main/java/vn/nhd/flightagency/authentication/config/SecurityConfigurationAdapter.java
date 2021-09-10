package vn.nhd.flightagency.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.nhd.flightagency.authentication.filter.CustomJwtAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity(debug = true)
@Component
//@Order(1)
public class SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    /*
    @Qualifier("customUserDetailsServiceImpl")
    @Autowired
    private UserDetailsSerice userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new SCryptPasswordEncoder());
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
     */

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(customJwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeRequests(auth -> auth
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/api/v1/account/**").permitAll()
                    .antMatchers("/api/v1/flight/**").permitAll()
                    //.antMatchers("/api/v1/test1/**").hasRole("USER")
                    //.antMatchers("/api/v1/flight-schedule/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                ).oauth2Login(oauth2 ->
                        oauth2.successHandler(new ForwardAuthenticationSuccessHandler("/api/v1/account/social/auth")));
                //.expressionHandler(securityExpressionHandler())
        //http.httpBasic(); // -> Forbidden if no using any authentication
        //http.cors();
    }
}
