package org.gestion.miniprojetjee.service;

import org.gestion.miniprojetjee.model.Commande;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.model.User;

import java.util.List;

public interface CommandeService {

    Long createCommandeFromPanier(User user, List<LigneCommande> panier);

    List<Commande> findAll();

    Commande findById(Long id);

    void delete(Long id);

    void updateStatus(Long id, String statut);
}
