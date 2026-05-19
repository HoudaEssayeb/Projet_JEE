package org.gestion.miniprojetjee.dao;

import org.gestion.miniprojetjee.model.Commande;

import java.util.List;

public interface CommandeDao {

    Long save(Commande commande);

    List<Commande> findAll();

    Commande findById(Long id);

    void delete(Long id);

    void updateStatus(Long id, String statut);
}
