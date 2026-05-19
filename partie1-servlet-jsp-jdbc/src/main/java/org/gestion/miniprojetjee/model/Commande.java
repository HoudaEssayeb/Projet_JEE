package org.gestion.miniprojetjee.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Commande {

    private Long id;
    private User user;
    private LocalDateTime dateCommande;
    private String statut;
    private List<LigneCommande> lignes = new ArrayList<>();

    public Commande() {
    }

    public Commande(Long id, User user, LocalDateTime dateCommande, String statut) {
        this.id = id;
        this.user = user;
        this.dateCommande = dateCommande;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public double getTotalCommande() {
        double total = 0;

        if (lignes != null) {
            for (LigneCommande ligne : lignes) {
                total += ligne.getTotal();
            }
        }

        return total;
    }
}
