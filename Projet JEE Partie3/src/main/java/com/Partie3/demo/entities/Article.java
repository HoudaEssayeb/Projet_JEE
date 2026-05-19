package com.Partie3.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "articles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Article {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private double prix;

        @Column(name = "date_expiration")
        private LocalDate dateExpiration;

        @Column(name = "quantite_stock", nullable = false)
        private int quantiteStock;
    }