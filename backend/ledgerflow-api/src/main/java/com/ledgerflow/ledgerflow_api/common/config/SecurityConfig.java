package com.ledgerflow.ledgerflow_api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
public class SecurityConfig {

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/wallets/**").permitAll() // Allow all requests to wallet endpoints
                .anyRequest().authenticated() // Require authentication for other requests
            )
           .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form.disable());// Use basic authentication

        return http.build();
    }

}
