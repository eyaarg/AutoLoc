# Stratégie de fetch et de cascade : AutoLoc

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat. Supprimer le contrat supprime ses paiements, et retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence. LAZY évite de charger tous les véhicules à chaque lecture d'une agence. |
| Agence → Employe | LAZY | Aucune | Un employé ne doit pas être supprimé automatiquement avec son agence. |
| Vehicule ↔ Equipement | LAZY | Aucune | Les équipements sont partagés entre plusieurs véhicules : supprimer un véhicule ne doit pas les supprimer. Un Set évite les doublons. |
| Client → Reservation | LAZY | PERSIST | Enregistrer un client enregistre ses nouvelles réservations. Pas de suppression en cascade pour conserver l'historique. |
| Reservation → Vehicule | LAZY | Aucune | Le véhicule existe indépendamment de ses réservations. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Le contrat découle de la réservation et n'a pas de sens sans elle. Limite : en Hibernate, le LAZY d'un OneToOne côté inverse n'est pas toujours effectif. |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance est enregistrée avec le véhicule. Pas de suppression en cascade pour garder l'historique. |

