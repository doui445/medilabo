# Gateway

Ce microservice est le point d'entrée de l'application. Il gère le routage vers les autres microservices à l'aide de **Spring Cloud Gateway**.

---

## Fonctionnement

Ce microservice reçoit les requêtes de l'UI et les rediriges automatiquement vers les microservices `patient`, `notes` ou `screening` en fonction du chemin de l'URL (`/api/patients/**`, `/api/notes/**`, `/api/screening/**`).

Il a également pour rôle de masquer l'architecture interne de l'application (le front-end ne connait que la Gateway).

Il est configuré sur le port 8090 et les routes sont configurées dans le fichier `GatewayApplication.java`.
