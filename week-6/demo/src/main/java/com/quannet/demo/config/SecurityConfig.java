package com.quannet.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.quannet.demo.repository.UserRepository;


@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(account -> org.springframework.security.core.userdetails.User
                        .withUsername(account.getUsername())
                        .password(account.getPassword())
                        .authorities(
                                account.getRole().startsWith("ROLE_")
                                        ? account.getRole()
                                        : "ROLE_" + account.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

   @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        request -> HttpMethod.POST.matches(request.getMethod())
                                && ("/api/users/add".equals(request.getServletPath())
                                || "/api/users/login".equals(request.getServletPath()))))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users/add", "/api/users/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
