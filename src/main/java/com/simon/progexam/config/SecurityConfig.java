package com.simon.progexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- Endpoints ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        // Offentlige endpoints
                        .requestMatchers("/api/sensor-data").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/", "/index.html", "/js/**", "/css/**").permitAll()

                        // USER endpoints
                        .requestMatchers("/api/warnings/active").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/warnings/*/reports").hasAnyRole("USER", "ADMIN")

                        // ADMIN endpoints
                        .requestMatchers("/api/sensor-reading-data").hasRole("ADMIN")
                        .requestMatchers("/api/warnings").hasRole("ADMIN")
                        .requestMatchers("/api/warnings/*/status").hasRole("ADMIN")
                        .requestMatchers("/api/warnings/*/reports/count").hasRole("ADMIN")
                        .requestMatchers("/api/warnings/*/readings").hasRole("ADMIN")
                        .requestMatchers("/api/warnings/reports").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/warnings/*/reports").hasAnyRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(401);
                        })
                );

        return http.build();
    }

    // --- Brugere ---
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    // --- Password encoder ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
