package com.Partie3.demo.config;

import com.Partie3.demo.entities.Article;
import com.Partie3.demo.entities.User;
import com.Partie3.demo.repository.ArticleRepository;
import com.Partie3.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            ArticleRepository articleRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            // =========================
            // ADMIN
            // =========================

            if (userRepository.findByUsername("admin") == null) {

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .nomComplet("Administrateur")
                        .telephone("0600000000")
                        .adresseLivraison("Rabat")
                        .build();

                userRepository.save(admin);

                System.out.println("ADMIN créé : admin / admin123");
            }

            // =========================
            // ARTICLES
            // =========================

            if (articleRepository.count() == 0) {

                articleRepository.save(
                        new Article(
                                null,
                                "PC HP Pavilion",
                                8500.0,
                                LocalDate.now().plusYears(2),
                                15
                        )
                );

                articleRepository.save(
                        new Article(
                                null,
                                "Clavier Gamer RGB",
                                450.0,
                                LocalDate.now().plusYears(3),
                                40
                        )
                );

                articleRepository.save(
                        new Article(
                                null,
                                "Souris Logitech",
                                220.0,
                                LocalDate.now().plusYears(2),
                                30
                        )
                );

                articleRepository.save(
                        new Article(
                                null,
                                "Écran Samsung 24 pouces",
                                1900.0,
                                LocalDate.now().plusYears(4),
                                12
                        )
                );

                articleRepository.save(
                        new Article(
                                null,
                                "Casque Bluetooth",
                                600.0,
                                LocalDate.now().plusYears(2),
                                25
                        )
                );

                System.out.println("Articles créés");
            }
        };
    }
}