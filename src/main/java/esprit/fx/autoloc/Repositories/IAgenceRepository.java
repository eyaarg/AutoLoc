package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Pas de @Repository : Spring Data crée tout seul l'implémentation (un proxy) au démarrage.
public interface IAgenceRepository extends JpaRepository<Agence,Long> {
}
