# Medilabo Solutions

Application basée sur une architecture en microservices, destinée au dépistage du diabète de type 2.
Cette application intègre un système de gestion des patients, un historique médical pour que le médecin puisse prendre des notes, ainsi qu'un système de détection des risques de diabète basé sur plusieurs niveaux :
  - aucun risque (None);
  - risque limité (Borderline);
  - danger (In Danger);
  - apparition précoce (Early onset).

---

## Architecture du Projet

L'application est découpée en plusieurs microservices indépendants communicants entre eux via une Gateway :
  - **UI :** Application Front-end qui gère l'interface utilisateur (HTML/Thymeleaf) en interagissant directement avec la Gateway.
  - **Gateway :** Point d'entrée de l'application qui gère le routage vers les bons microservices.
  - **API Patient :** Microservice qui s'occupe de la gestion des patients et de leurs données. Il est relié à une base de données PostgresSQL.
  - **API Notes :** Microservice qui s'occupe de l'historique médical et des notes des médecins. Il est relié à une base de données NoSQL MongoDB.
  - **API Screening :** Microservice qui s'occupe du dépistage du diabète chez les patients. Il évalue les données du patient ainsi que les notes du médecin pour attribuer un risque correspondant (None, Borderline, In Danger, Early Onset).

---

## Exigences du client

L'application a été conçue en suivant les directives suivantes du client :
  - Les bases de données sont toutes normalisées 3NF, elles ne contiennent pas de doublons ou de redondance.
  - L'accès aux données des patients est sécurisé avec un système d'authentification Spring Sécurity.
  - Le Green Code qui a pour but de protéger l'environnement a été étudié et plusieurs éco-recommandations ont été mises en place.

### Un petit mot sur le Green Code :
Le Green Code est une démarche d'éco conception de nos applications visant à minimiser l'impact environnemental.
Pour faire du Green Code, il faut adopter une vision orientée optimisation, que ce soit sur l'UX et l'UI restant sobre et en permettant aux utilisateurs de trouver rapidement ce qu'ils sont venus chercher.
Ou bien sur le back, en ayant un code réactif, sans déchets et en réduisant au maximum le nombre de lignes ou de calculs.
Ou bien encore que ce soit sur l'architecture de nos applications et bases de données en essayant de réduire au maximum l'impact carbone qu'elles vont avoir une fois déployé.
