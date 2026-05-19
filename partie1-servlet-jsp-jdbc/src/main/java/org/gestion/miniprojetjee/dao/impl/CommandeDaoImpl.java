package org.gestion.miniprojetjee.dao.impl;

import org.gestion.miniprojetjee.dao.CommandeDao;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.model.Commande;
import org.gestion.miniprojetjee.model.LigneCommande;
import org.gestion.miniprojetjee.model.User;
import org.gestion.miniprojetjee.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CommandeDaoImpl implements CommandeDao {

    @Override
    public Long save(Commande commande) {
        String sqlCommande = "INSERT INTO commandes(user_id, statut) VALUES (?, ?)";
        String sqlLigne = "INSERT INTO ligne_commandes(commande_id, article_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        String sqlStockCheck = "SELECT quantite_stock FROM articles WHERE id = ? FOR UPDATE";
        String sqlUpdateStock = "UPDATE articles SET quantite_stock = quantite_stock - ? WHERE id = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            Long commandeId;

            try (PreparedStatement psCommande = connection.prepareStatement(sqlCommande, Statement.RETURN_GENERATED_KEYS)) {
                psCommande.setLong(1, commande.getUser().getId());
                psCommande.setString(2, commande.getStatut());

                psCommande.executeUpdate();

                ResultSet generatedKeys = psCommande.getGeneratedKeys();

                if (generatedKeys.next()) {
                    commandeId = generatedKeys.getLong(1);
                } else {
                    throw new RuntimeException("Erreur lors de la création de la commande");
                }
            }

            for (LigneCommande ligne : commande.getLignes()) {
                Long articleId = ligne.getArticle().getId();
                int quantiteDemandee = ligne.getQuantite();

                try (PreparedStatement psStock = connection.prepareStatement(sqlStockCheck)) {
                    psStock.setLong(1, articleId);

                    ResultSet rs = psStock.executeQuery();

                    if (!rs.next()) {
                        throw new RuntimeException("Article introuvable");
                    }

                    int stockActuel = rs.getInt("quantite_stock");

                    if (quantiteDemandee > stockActuel) {
                        throw new RuntimeException("Stock insuffisant pour l'article : " + ligne.getArticle().getDescription());
                    }
                }

                try (PreparedStatement psLigne = connection.prepareStatement(sqlLigne)) {
                    psLigne.setLong(1, commandeId);
                    psLigne.setLong(2, articleId);
                    psLigne.setInt(3, quantiteDemandee);
                    psLigne.setDouble(4, ligne.getPrixUnitaire());

                    psLigne.executeUpdate();
                }

                try (PreparedStatement psUpdateStock = connection.prepareStatement(sqlUpdateStock)) {
                    psUpdateStock.setInt(1, quantiteDemandee);
                    psUpdateStock.setLong(2, articleId);

                    psUpdateStock.executeUpdate();
                }
            }

            connection.commit();
            return commandeId;

        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();

        String sql = """
                SELECT c.id, c.date_commande, c.statut,
                       u.id AS user_id, u.username, u.password, u.role
                FROM commandes c
                JOIN users u ON c.user_id = u.id
                ORDER BY c.id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Commande commande = new Commande();

                commande.setId(rs.getLong("id"));
                commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
                commande.setStatut(rs.getString("statut"));

                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                commande.setUser(user);

                commandes.add(commande);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandes;
    }

    @Override
    public Commande findById(Long id) {
        Commande commande = null;

        String sqlCommande = """
                SELECT c.id, c.date_commande, c.statut,
                       u.id AS user_id, u.username, u.password, u.role
                FROM commandes c
                JOIN users u ON c.user_id = u.id
                WHERE c.id = ?
                """;

        String sqlLignes = """
                SELECT lc.id, lc.quantite, lc.prix_unitaire,
                       a.id AS article_id, a.description, a.prix, a.date_expiration, a.quantite_stock
                FROM ligne_commandes lc
                JOIN articles a ON lc.article_id = a.id
                WHERE lc.commande_id = ?
                """;

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(sqlCommande)) {
                ps.setLong(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    commande = new Commande();

                    commande.setId(rs.getLong("id"));
                    commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
                    commande.setStatut(rs.getString("statut"));

                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    commande.setUser(user);
                }
            }

            if (commande == null) {
                return null;
            }

            List<LigneCommande> lignes = new ArrayList<>();

            try (PreparedStatement ps = connection.prepareStatement(sqlLignes)) {
                ps.setLong(1, id);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Article article = new Article();

                    article.setId(rs.getLong("article_id"));
                    article.setDescription(rs.getString("description"));
                    article.setPrix(rs.getDouble("prix"));

                    Date dateExpiration = rs.getDate("date_expiration");
                    if (dateExpiration != null) {
                        article.setDateExpiration(dateExpiration.toLocalDate());
                    }

                    article.setQuantiteStock(rs.getInt("quantite_stock"));

                    LigneCommande ligne = new LigneCommande();
                    ligne.setId(rs.getLong("id"));
                    ligne.setCommande(commande);
                    ligne.setArticle(article);
                    ligne.setQuantite(rs.getInt("quantite"));
                    ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));

                    lignes.add(ligne);
                }
            }

            commande.setLignes(lignes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return commande;
    }

    @Override
    public void delete(Long id) {
        String sqlDeleteLignes = "DELETE FROM ligne_commandes WHERE commande_id = ?";
        String sqlDeleteCommande = "DELETE FROM commandes WHERE id = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteLignes)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = connection.prepareStatement(sqlDeleteCommande)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateStatus(Long id, String statut) {
        String sql = "UPDATE commandes SET statut = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
