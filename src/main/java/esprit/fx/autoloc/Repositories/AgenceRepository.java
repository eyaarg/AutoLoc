package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenceRepository extends JpaRepository<Agence,Long> {
}
