package edu.icet.shopsphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for API testing on Postman
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll() // allow unauthenticated access to auth endpoints
                    .anyRequest().authenticated() // require authentication for all other endpoints
            )
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}
