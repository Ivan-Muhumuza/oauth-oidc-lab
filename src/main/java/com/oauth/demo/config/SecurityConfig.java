package com.oauth.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf() // CSRF protection is enabled by default
                .and()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers( "/login", "logout").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                )
                .sessionManagement(session ->
                        session
                                .sessionFixation().migrateSession() // Protect against session fixation
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session only if required
                                .maximumSessions(1) // Limit to one session per user
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // URL to trigger logout
                                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                                .invalidateHttpSession(true) // Invalidate the session
                                .clearAuthentication(true) // Clear authentication
                                .deleteCookies("JSESSIONID") // Delete session cookie
                );

        return http.build();
    }
}

