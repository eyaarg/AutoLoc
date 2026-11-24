# Notes : Couche Repository (Atelier 3)

## Choix d'interface

J'ai utilisé `JpaRepository<Entité, Long>` pour les 9 repositories. Elle regroupe le CRUD,
les méthodes qui renvoient des `List`, le tri, la pagination et `flush`. Toutes les clés
primaires sont de type `Long`. Pas besoin de `@Repository` : Spring Data génère le proxy au démarrage.

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, listes, tri et pagination. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet, listes, tri et pagination. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, pagination utile pour la liste des véhicules. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet et listes. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, listes, tri et pagination. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet, listes, tri et pagination. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Créé pour lire les paiements ; la création et la suppression passent par Contrat. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet et listes. |

## Comportement observé (programme de démonstration)

- `save` avec id null : INSERT, l'id est rempli par MySQL. Avec un id existant : SELECT puis UPDATE.
- `findById` renvoie un `Optional`, vide si l'id n'existe pas.
- `deleteById` supprime le contrat et ses paiements (cascade + orphanRemoval). Aucune erreur si l'id n'existe pas.
- `deleteAllInBatch` est évité sur Contrat : il contourne la cascade et échouerait à cause de la clé étrangère des paiements.

## Anomalies SonarQube for IDE corrigées

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Imports inutilisés dans Paiement | Code mort | Imports `ArrayList` et `List` supprimés |
| `@Repository` sur les interfaces | Inutile avec Spring Data | Annotation supprimée |
| Champ `vehicule` dans Vehicule | Auto-référence inutile | Champ supprimé |
| Commentaire inutile dans Reservation | Code mort | Commentaire supprimé |
| Champ `reservation` après les méthodes dans Contrat | Ordre des membres de la classe | Champ remonté avec les autres attributs |