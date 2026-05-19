# Fiche de démonstration — Partie 1

## Sujet

Mini Projet JEE — Partie 1  
Développement avec Servlet, JSP, JSTL, Filtres, Bootstrap et JDBC.

## Objectif

Développer une application web permettant de gérer des articles, un panier et des commandes avec authentification.

## Technologies utilisées

- JDK 17
- Tomcat 10
- Jakarta EE
- Servlet
- JSP
- JSTL
- JDBC
- MySQL
- Bootstrap
- Maven

## Architecture utilisée

L'application respecte une architecture en couches :

```text
Présentation JSP
        ↓
Servlet Controller
        ↓
Service
        ↓
DAO JDBC
        ↓
Base de données MySQL