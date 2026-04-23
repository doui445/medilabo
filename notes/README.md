# Notes

Ce microservice gère l'historique médical des patients. Il permet aux médecins d'ajouter, de modifier ou de consulter des notes associées aux patients.

---

## Fonctionnement

Ce microservice reçoit les requêtes de l'UI via la Gateway, valide les notes et traite les modifications à apporter à la base de données.

Il fonctionne avec une base de données NoSQL MangoDB qui est configurée de la manière suivante : `spring.mongodb.uri=mongodb://localhost:27017/medilabo_notes`.

Il met à disposition les routes suivantes à l'adresse `/api/notes` :

| Méthode  | Endpoint               | Description                                          |
|:---------|:-----------------------|:-----------------------------------------------------|
| `GET`    | `/patient/{patientId}` | Récupère l'historique complet des notes d'un patient |
| `GET`    | `/{id}`                | Récupère une note spécifique via son ID              |
| `POST`   | `/`                    | Crée une nouvelle note                               |
| `PUT`    | `/{id}`                | Met à jour le contenu d'une note existante           |
| `DELETE` | `/{id}`                | Supprime une note                                    |

Il est configuré sur le port 8082 qui n'est pas exposé au front-end.
