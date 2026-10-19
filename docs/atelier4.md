# Atelier 4 : couche Service, IoC et injection de dépendances

## A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par attribut |
|---|---|---|
| Champ `final` possible | Oui | Non |
| Dépendance visible dans la signature de la classe | Oui | Non |
| Utilisable avec `new` hors conteneur sans dépendance `null` | Oui, le repository est obligatoire pour créer l'objet | Non, le champ reste `null` |
| Signalé par SonarQube for IDE | Non | Oui (java:S6813) |

Avec l'injection par attribut, l'application démarre normalement, mais SonarQube signale la règle S6813 (« Field dependency injection should be avoided »). Avec l'injection par constructeur, le service ne peut pas exister sans son repository. C'est la convention retenue pour tous les services d'AutoLoc.

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| ContratServiceImpl | ContratRepository | Constructeur | Champ `final`, objet jamais créé incomplet, constructeur unique donc pas besoin de `@Autowired` |
| AgenceServiceImpl | AgenceRepository | Constructeur | Même justification |
| EmployeServiceImpl | EmployeRepository | Constructeur | Même justification |
| VehiculeServiceImpl | VehiculeRepository | Constructeur | Même justification |
| EquipementServiceImpl | EquipementRepository | Constructeur | Même justification |
| ClientServiceImpl | ClientRepository | Constructeur | Même justification |
| ReservationServiceImpl | ReservationRepository | Constructeur | Même justification |
| PaiementServiceImpl | PaiementRepository | Constructeur | Même justification, service en lecture seule (findById et findAll) |
| MaintenanceServiceImpl | MaintenanceRepository | Constructeur | Même justification |

## C. Messages d'erreur

**Message 1 : NoSuchBeanDefinitionException.**
Le bean manquant est `IContratService`, demandé par le paramètre 0 du constructeur de `ContratController`. Côté `ContratServiceImpl`, il y a deux causes probables : l'annotation `@Service` est oubliée, donc Spring ne crée aucun bean, ou bien la classe est placée en dehors du package racine `esprit.fx.autoloc`, donc le scan ne la détecte jamais. Correction : ajouter `@Service` et vérifier que la classe est dans `esprit.fx.autoloc` ou un de ses sous-packages.

**Message 2 : NoUniqueBeanDefinitionException.**
`NotificationController` reçoit deux beans du même type, `emailNotificateur` et `smsNotificateur`, et Spring ne sait pas lequel choisir. Première correction : poser `@Primary` sur la classe du bean choisi par défaut, par exemple `EmailNotificateur`. Deuxième correction : poser `@Qualifier("smsNotificateur")` sur le paramètre du constructeur du contrôleur, pour désigner le bean voulu par son nom.

**Message 3 : dépendance circulaire (BeanCurrentlyInCreationException).**
`clientServiceImpl` et `reservationServiceImpl` s'injectent mutuellement. Avec l'injection par constructeur, pour créer l'un il faut que l'autre existe déjà, et inversement, donc aucun des deux ne peut être créé en premier et le cycle ne peut pas être contourné. Piste de conception : extraire la responsabilité commune dans un troisième service dont les deux dépendent, ou faire dépendre l'un des services du repository de l'autre plutôt que de son service.

## D. Anomalies SonarQube for IDE

| Anomalie | Règle | Explication | Correction |
|---|---|---|---|
| Injection par attribut avec `@Autowired` | java:S6813 | Le champ ne peut pas être `final`, la dépendance est cachée et l'objet peut exister avec un champ `null` hors conteneur | Retour à l'injection par constructeur avec `@RequiredArgsConstructor` et un champ `final` |
| Import inutile `lombok.RequiredArgsConstructor` | java:S1128 | L'import n'était plus utilisé car l'annotation était commentée | Import de nouveau utilisé après avoir restauré `@RequiredArgsConstructor` |
| Exception générique `RuntimeException` | java:S112 | Une exception générique ne permet pas de savoir précisément ce qui a échoué | Utilisation de l'exception métier `ResourceNotFoundException` |

## E. Questions de compréhension

**1. Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
Il n'y en a aucun dans notre code. Le conteneur Spring (`ApplicationContext`, créé par `SpringApplication.run`) crée le bean au démarrage, après avoir détecté `@Service` pendant le scan du package racine. Il lui fournit le proxy de `ContratRepository` par son constructeur.

**2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
Pour limiter le couplage. Le contrôleur ne connaît que le contrat de l'interface, donc on peut changer l'implémentation sans modifier le contrôleur, et on peut fournir une fausse implémentation pour les tests.

**3. Pourquoi un service singleton doit-il rester sans état ?**
Une seule instance du service est partagée par toutes les requêtes, qui peuvent s'exécuter en même temps. Exemple de bug : si le service garde un attribut `Contrat contratCourant`, la requête d'un client peut l'écraser pendant qu'une autre requête l'utilise, et le deuxième client voit alors le contrat du premier.

**4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` ?**
Passer directement l'objet reçu à `save` écraserait toutes les colonnes : les champs non envoyés deviendraient `null`. Pour le contrat, avec `orphanRemoval = true`, une liste de paiements vide pourrait aussi supprimer tous les paiements existants. On charge donc l'existant et on ne recopie que les attributs modifiables.

**5. Différences entre les annotations, et rôle de `IPaiementService`.**
`@Component` se pose sur une classe qu'on écrit, que le scan détecte. `@Bean` se pose sur une méthode d'une classe `@Configuration`, pour une classe qu'on ne peut pas annoter (bibliothèque, JDK). `@Primary` se pose sur la classe et désigne le candidat par défaut quand plusieurs beans ont le même type. `@Qualifier` se pose au point d'injection et choisit un bean précis par son nom. `IPaiementService` n'expose ni création, ni modification, ni suppression, car les paiements se gèrent à travers le contrat (`addPaiement`, cascade et `orphanRemoval`), ce qui garde le contrat cohérent.