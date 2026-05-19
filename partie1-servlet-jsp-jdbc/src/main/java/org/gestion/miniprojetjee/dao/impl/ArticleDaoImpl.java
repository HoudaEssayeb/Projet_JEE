package org.gestion.miniprojetjee.dao.impl;

import org.gestion.miniprojetjee.dao.ArticleDao;
import org.gestion.miniprojetjee.model.Article;
import org.gestion.miniprojetjee.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArticleDaoImpl implements ArticleDao {

    @Override
    public void save(Article article) {
        String sql = "INSERT INTO articles(description, prix, date_expiration, quantite_stock) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, article.getDescription());
            ps.setDouble(2, article.getPrix());

            if (article.getDateExpiration() != null) {
                ps.setDate(3, Date.valueOf(article.getDateExpiration()));
            } else {
                ps.setDate(3, null);
            }

            ps.setInt(4, article.getQuantiteStock());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Article article) {
        String sql = "UPDATE articles SET description = ?, prix = ?, date_expiration = ?, quantite_stock = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, article.getDescription());
            ps.setDouble(2, article.getPrix());

            if (article.getDateExpiration() != null) {
                ps.setDate(3, Date.valueOf(article.getDateExpiration()));
            } else {
                ps.setDate(3, null);
            }

            ps.setInt(4, article.getQuantiteStock());
            ps.setLong(5, article.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Article findById(Long id) {
        String sql = "SELECT * FROM articles WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToArticle(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();

        String sql = "SELECT * FROM articles ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    @Override
    public List<Article> search(String description, Double prixMin, Double prixMax) {
        List<Article> articles = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM articles WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (description != null && !description.trim().isEmpty()) {
            sql.append(" AND description LIKE ?");
            params.add("%" + description + "%");
        }

        if (prixMin != null) {
            sql.append(" AND prix >= ?");
            params.add(prixMin);
        }

        if (prixMax != null) {
            sql.append(" AND prix <= ?");
            params.add(prixMax);
        }

        sql.append(" ORDER BY id DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    private Article mapResultSetToArticle(ResultSet rs) throws Exception {
        Article article = new Article();

        article.setId(rs.getLong("id"));
        article.setDescription(rs.getString("description"));
        article.setPrix(rs.getDouble("prix"));

        Date dateExpiration = rs.getDate("date_expiration");
        if (dateExpiration != null) {
            article.setDateExpiration(dateExpiration.toLocalDate());
        }

        article.setQuantiteStock(rs.getInt("quantite_stock"));

        return article;
    }
}