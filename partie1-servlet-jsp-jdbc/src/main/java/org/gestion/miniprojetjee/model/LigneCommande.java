package org.gestion.miniprojetjee.model;

public class LigneCommande {

    private Long id;
    private Commande commande;
    private Article article;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {
    }

    public LigneCommande(Long id, Commande commande, Article article, int quantite, double prixUnitaire) {
        this.id = id;
        this.commande = commande;
        this.article = article;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getId() {
        return id;
    }

    public Commande getCommande() {
        return commande;
    }

    public Article getArticle() {
        return article;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getTotal() {
        return quantite * prixUnitaire;
    }
}
