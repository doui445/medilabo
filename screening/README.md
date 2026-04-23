# Screening

Ce microservice contient la logique métier de l'application. Il permet de dépister le diabète de type 2 chez un patient.

---

## Fonctionnement

Ce microservice reçoit les requêtes de l'UI via la Gateway, intéragis avec les microservices patient et notes pour récupérer les données, puis analyses ces données pour déterminer si un patient est à risque ou non.

Les niveaux de risque dépendent de l'âge et du genre du patient, mais également du nombre de mots clés "déclencheurs" qui vont être trouvés dans les notes du médecin.
Il y a 4 niveaux de risque : `NONE`, `BORDERLINE`, `IN DANGER`, et `EARLY ONSET`.

Il met à disposition la route suivante à l'adresse `/api/screening` :

| Méthode | Endpoint       | Description                                   |
|:--------|:---------------|:----------------------------------------------|
| `GET`   | `/{patientId}` | Renvoie le résultat du dépistage d'un patient |

Il est configuré sur le port 8083 qui n'est pas exposé au front-end.
