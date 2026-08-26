/*
 * 12_Spring_Boot_Security_Session.java
 *
 * Spring Security session configuration.
 *
 * Add:
 * spring-boot-starter-security
 *
 * Modern Spring Security uses SecurityFilterChain rather than the old
 * WebSecurityConfigurerAdapter approach.
 */
package com.example.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecuritySessionConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/public/logout-success")
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                .sessionFixation(fixation -> fixation.changeSessionId())
            );

        return http.build();
    }
}

/*
 * Important:
 * - Authentication state can be persisted using the HTTP session.
 * - The browser commonly sends a session cookie containing a session ID.
 * - Session fixation protection changes the session identifier after
 *   authentication.
 * - Never disable session-fixation protection without a strong reason.
 *
 * For production:
 * - HTTPS
 * - secure cookies
 * - HttpOnly cookies
 * - suitable SameSite policy
 * - CSRF protection appropriate to your application
 */
