# PlaniFem – API de gestion de la planification familiale

API REST (Spring Boot 4, Java 21) pour gérer le suivi des patientes de planification familiale dans les postes de santé : inscription, consultations, méthodes contraceptives, visites de suivi, rendez-vous, rapports mensuels et tableaux de bord. Projet de mémoire de Master 2 en Ingénierie Logicielle (UN-CHK).

## Fonctionnalités

- Authentification JWT et gestion des rôles : super administrateur, administrateur de poste, sage-femme (accès limité au poste de santé)
- Gestion des postes de santé, des utilisateurs et des patientes (cycle de vie, statut VIH, ARV)
- Fiches de consultation, changements de méthode contraceptive, visites de suivi, rendez-vous
- Rapports mensuels et statistiques (tableaux de bord par poste et au niveau national)
- Documentation interactive de l'API avec Swagger UI

## Architecture

Architecture hexagonale (ports et adaptateurs) organisée en modules métier : `user`, `postesante`, `adminposte`, `patient`, `consultation`, `contraception`, `appointment`, `rapport`, `dashboard`.

Chaque module sépare `domain` (modèle, ports), `application` (services) et `infrastructure` (REST, persistance, sécurité). Le schéma de la base est versionné avec Flyway.

## Stack technique

- Java 21, Spring Boot 4 (Web MVC, Data JPA, Security, Validation, Actuator)
- JWT (jjwt), PostgreSQL, Flyway, springdoc-openapi, Lombok
- Docker, Maven
- Déploiement : Render (Docker) avec PostgreSQL managé (Neon)

## Lancer le projet

### Avec Docker (le plus simple)

```bash
docker compose up --build
```

L'API démarre sur http://localhost:8081 avec une base PostgreSQL 16. Les migrations Flyway s'exécutent au démarrage.

### En local avec Maven

Prérequis : Java 21 et un PostgreSQL local (`pf_sagefemme`).

```bash
./mvnw spring-boot:run
```

Variables utiles : `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `BOOTSTRAP_SUPER_ADMIN_EMAIL`, `BOOTSTRAP_SUPER_ADMIN_PASSWORD`.

## Documentation de l'API

Swagger UI : http://localhost:8081/swagger-ui.html (spécification OpenAPI : `/v3/api-docs`).

## Sécurité

En production, définir `JWT_SECRET` et les identifiants du super administrateur par variables d'environnement. Ne jamais utiliser les valeurs par défaut du code.

## Auteur

Elimane Gning – [LinkedIn](https://linkedin.com/in/elimane-gning)
