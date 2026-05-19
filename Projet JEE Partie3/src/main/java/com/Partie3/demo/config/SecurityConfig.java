package com.Partie3.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                // Autorisations
                .authorizeHttpRequests(auth -> auth

                        // Routes ADMIN
                        .requestMatchers(
                                "/admin/**",
                                "/articles/nouveau",
                                "/articles/modifier/**",
                                "/articles/supprimer/**"
                        ).hasRole("ADMIN")

                        // Routes USER + ADMIN
                        .requestMatchers(
                                "/articles",
                                "/panier/**",
                                "/profil"
                        ).hasAnyRole("USER", "ADMIN")

                        // Routes publiques
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/css/**",
                                "/js/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // Form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCrypt sécurisé

        return new BCryptPasswordEncoder();
    }
}