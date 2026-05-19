package org.gestion.miniprojetjee.model;

import java.time.LocalDate;

public class Article {

    private Long id;
    private String description;
    private double prix;
    private LocalDate dateExpiration;
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