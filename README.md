# Boutique de Thés - Documentation

## 📋 Vue d'ensemble

**Boutique de Thés** est une application web Spring Boot pour la gestion d'une boutique de thés. Elle permet de gérer les produits avec une interface web interactive.

- **Langage** : Java 17
- **Framework** : Spring Boot 3.2.0
- **Base de données** : MySQL
- **Build** : Maven

## 🏗️ Architecture

```
com.boutique.thes/
├── controller/      # Contrôleurs HTTP
├── service/         # Logique métier
├── model/           # Entités JPA
└── repository/      # Accès aux données
```

## 📦 Dépendances principales

| Dépendance | Version | Rôle |
|-----------|---------|------|
| spring-boot-starter-web | 3.2.0 | API REST et MVC |
| spring-boot-starter-data-jpa | 3.2.0 | ORM et persistance |
| spring-boot-starter-thymeleaf | 3.2.0 | Templates HTML |
| spring-boot-starter-validation | 3.2.0 | Validation des données |
| mysql-connector-j | Latest | Driver MySQL |
| lombok | Latest | Réduction de boilerplate |

## 🔌 Endpoints disponibles

### Produits
- `GET /produits` - Lister tous les produits
- `GET /produits/{id}` - Récupérer un produit
- `POST /produits` - Créer un produit
- `PUT /produits/{id}` - Modifier un produit
- `DELETE /produits/{id}` - Supprimer un produit

## 🗂️ Modèle de données

**Produit** (`Produit.java`)
- `id` : identifiant unique
- `nom` : nom du produit
- `description` : description
- `prix` : prix unitaire
- `stock` : quantité en stock

## 🚀 Démarrage

```bash
./mvnw spring-boot:run
```

Application accessible sur : `http://localhost:8080`

## ⚙️ Configuration

Éditer `application.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/boutique_thes
spring.datasource.username=root
spring.datasource.password=
```
