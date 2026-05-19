package org.gestion.miniprojetjee.service.impl;

import org.gestion.miniprojetjee.dao.CommandeDao;
import org.gestion.miniprojetjee.dao.impl.CommandeDaoImpl;
import org.gestion.miniprojetjee.model.Commande;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.model.User;
import org.gestion.miniprojetjee.service.CommandeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeServiceImpl implements CommandeService {

    private final CommandeDao commandeDao = new CommandeDaoImpl();

    @Override
    public Long createCommandeFromPanier(User user, List<LigneCommande> panier) {
        if (user == null) {
            throw new RuntimeException("Utilisateur non connecté");
        }

        if (panier == null || panier.isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        Commande commande = new Commande();
        commande.setUser(user);
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("VALIDEE");
        commande.setLignes(new ArrayList<>(panier));

        return commandeDao.save(commande);
    }

    @Override
    public List<Commande> findAll() {
        return commandeDao.findAll();
    }

    @Override
    public Commande findById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return commandeDao.findById(id);
    }

    @Override
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID commande invalide");
        }

        commandeDao.delete(id);
    }

    @Override
    public void updateStatus(Long id, String statut) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID commande invalide");
        }

        if (statut == null || statut.trim().isEmpty()) {
            throw new RuntimeException("Le statut est obligatoire");
        }

        if (!isValidStatus(statut)) {
            throw new RuntimeException("Statut invalide");
        }

        commandeDao.updateStatus(id, statut);
    }

    private boolean isValidStatus(String statut) {
        return statut.equals("VALIDEE")
                || statut.equals("EN_PREPARATION")
                || statut.equals("LIVREE")
                || statut.equals("ANNULEE");
    }
}
