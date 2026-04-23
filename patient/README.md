# Notes

Ce microservice gère les données des patients. Il permet aux organisateurs et aux médecins d'ajouter, de modifier, de consulter ou de supprimer des patients.

---

## Fonctionnement

Ce microservice reçoit les requêtes de l'UI via la Gateway, valide les patients et traite les modifications à apporter à la base de données.

Il fonctionne avec une base de données PostgresSQL qui est normalisée au format 3NF et configurée de la manière suivante : `spring.datasource.url=jdbc:postgresql://localhost:5432/medilabo_patient`.

Il met à disposition les routes suivantes à l'adresse `/api/patients` :

| Méthode  | Endpoint | Description                              |
|:---------|:---------|:-----------------------------------------|
| `GET`    | `/`      | Récupère la liste de tous les patients   |
| `GET`    | `/{id}`  | Récupère les informations d'un patient   |
| `POST`   | `/`      | Ajoute un nouveau patient                |
| `PUT`    | `/{id}`  | Met à jour les informations d'un patient |
| `DELETE` | `/{id}`  | Supprime un patient                      |

Il est configuré sur le port 8081 qui n'est pas exposé au front-end.
