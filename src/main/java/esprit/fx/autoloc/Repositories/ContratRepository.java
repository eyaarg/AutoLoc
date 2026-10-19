package esprit.fx.autoloc.Repositories;


import esprit.fx.autoloc.Entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {}
