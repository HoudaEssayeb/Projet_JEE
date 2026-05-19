# Fiche de démonstration — Partie 2

## Sujet

Mini Projet JEE — Partie 2  
Développement avec Servlet, JSP, JSTL, Bootstrap et JPA.

## Objectif

Reprendre l'application de la Partie 1 et remplacer l'accès aux données JDBC par JPA.

## Technologies utilisées

- JDK 17
- Tomcat 10
- Jakarta EE
- Servlet
- JSP
- JSTL
- JPA
- Hibernate
- MySQL
- Bootstrap
- Maven

## Architecture utilisée

```text
Présentation JSP
        ↓
Servlet Controller
        ↓
Service
        ↓
DAO JPA
        ↓
EntityManager
        ↓
Base de données MySQL