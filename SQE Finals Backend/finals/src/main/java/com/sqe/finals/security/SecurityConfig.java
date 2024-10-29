package com.sqe.finals.security;

import com.sqe.finals.service.CustomAdminDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAdminDetailsService customAdminDetailsService;

    public SecurityConfig(CustomAdminDetailsService customAdminDetailsService) {
        this.customAdminDetailsService = customAdminDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Store CSRF token in a cookie
                        .ignoringRequestMatchers("/admin/login") // Allow login requests without CSRF
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/login").permitAll() // Allow login for all
                        .requestMatchers("/admin/register").hasRole("ADMIN") // Only authenticated admins can access /register
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict other /admin endpoints to admins only
                        .anyRequest().permitAll() // Permit other endpoints
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Allow sessions to be created if required
                )
                .addFilterAfter((request, response, chain) -> {
                    // Log the CSRF token generated
                    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    if (csrfToken != null) {
                        System.out.println("Generated CSRF Token: " + csrfToken.getToken());
                    }
                    chain.doFilter(request, response);
                }, CsrfFilter.class); // Log after CSRF filter

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}





