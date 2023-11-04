package com.example.demo;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties
public class OAuthSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, OAuth2AuthorizedClientRepository clientRepo) throws Exception {
        http
                .securityMatcher("/api/web/**", "/oauth/**", "/login/**") // TODO comment me to fix the stuff
                .authorizeHttpRequests((spec) -> spec

                        .requestMatchers("/api/web/auth/info")
                        .permitAll()

                        .requestMatchers("/api/web/auth/csrf")
                        .permitAll()

                        .requestMatchers("/api/web/**")
                        .authenticated()

                        .anyRequest()
                        .permitAll()
                )
                .cors(withDefaults())
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .oauth2Login(withDefaults())
                .oauth2Client(c -> c.authorizedClientRepository(clientRepo))
//                .logout((spec) -> spec.logoutUrl("/api/web/auth/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies(COOKIES_TO_DELETE));

        ;
        return http.build();
    }

    @Bean
    OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }

}

