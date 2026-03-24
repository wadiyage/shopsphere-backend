package edu.icet.shopsphere.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncorder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for API testing on Postman
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ) // use stateless sessions (no HTTP session)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll() // allow unauthenticated access to auth endpoints
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // restrict admin endpoints to users with ADMIN role
                    .requestMatchers("/api/user/cart/**").hasRole("CUSTOMER") // restrict cart endpoints to users with CUSTOMER role
                    .requestMatchers("/api/user/orders/**").hasRole("CUSTOMER") // restrict order endpoints to users with CUSTOMER role
                    .requestMatchers("/api/user/**").hasAnyRole("CUSTOMER", "ADMIN") // allow user endpoints for CUSTOMER and ADMIN roles
                    .requestMatchers("/api/products/**").permitAll() // allow unauthenticated access to product endpoints
                    .anyRequest().authenticated() // require authentication for all other endpoints
            )
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((request, response, ex) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                    )
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
