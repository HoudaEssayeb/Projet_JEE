package org.gestion.miniprojetjee.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private double prix;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "quantite_stock", nullable = false)
    private int quantiteStock;

    public Article() {
    }

    public Article(Long id, String description, double prix, LocalDate dateExpiration, int quantiteStock) {
        this.id = id;
        this.description = description;
        this.prix = prix;
        this.dateExpiration = dateExpiration;
        this.quantiteStock = quantiteStock;
    }

    public Article(String description, double prix, LocalDate dateExpiration, int quantiteStock) {
        this.description = description;
        this.prix = prix;
        this.dateExpiration = dateExpiration;
        this.quantiteStock = quantiteStock;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrix() {
        return prix;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
}