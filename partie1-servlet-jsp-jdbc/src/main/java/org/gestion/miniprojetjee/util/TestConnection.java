package org.gestion.miniprojetjee.util;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection connection = DBConnection.getConnection();
            System.out.println("Connexion MySQL réussie !");
            connection.close();
        } catch (Exception e) {
            System.out.println("Erreur de connexion MySQL");
            e.printStackTrace();
        }
    }
}
