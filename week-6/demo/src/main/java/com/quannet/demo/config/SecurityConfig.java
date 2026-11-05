package com.quannet.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quannet.demo.repository.UserRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(account -> {
                    String role = (account.getRole() != null) ? account.getRole() : "USER";
                    String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                    return org.springframework.security.core.userdetails.User
                            .withUsername(account.getUsername())
                            .password(account.getPassword())
                            .authorities(authority)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/users/add", "/api/users/login").permitAll()

                        // Foods & Computers
                        .requestMatchers(HttpMethod.GET, "/api/foods/**", "/api/computers/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/foods/**", "/api/computers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/foods/**", "/api/computers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/foods/**", "/api/computers/**").hasRole("ADMIN")

                        // Orders
                        .requestMatchers(HttpMethod.POST, "/api/orders/create").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/orders/**").hasRole("ADMIN")

                        // Sessions
                        .requestMatchers("/api/sessions/**").hasAnyRole("USER", "ADMIN")

                        // Users
                        .requestMatchers(HttpMethod.POST, "/api/users/nap-tien").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
