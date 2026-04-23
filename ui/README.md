# Gateway

Ce microservice correspond au front-end de l'application. Il affiche les pages web (HTML) aux organisateurs et aux médecins.

---

## Fonctionnement

Ce microservice reçoit les requêtes de l'utilisateur et les redirige automatiquement vers la gateway.

Il gère également la sécurité de l'application (l'authentification des utilisateurs) et l'accès aux données en fonction des rôles (`ORGANIZER` ou `DOCTOR`).

Il est configuré sur le port 8080 qui est le seul port visible pour les utilisateurs.

### Note aux développeurs
Pour l'instant l'application est encore en phase de tests, vous pouvez vous y connecter via les identifiants suivants :

| Identifiant  | Mot de passe     | Rôle        |
|:-------------|:-----------------|:------------|
| medilabo.org | `Password123`    | `ORGANIZER` |
| medilabo.doc | `DocMedilab123%` | `DOCTOR`    |

Ces identifiants seront ensuite supprimés et remplacés pour correspondre aux besoins réels du client.