package esprit.fx.autoloc.Repositories;


import esprit.fx.autoloc.Entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IContratRepository extends JpaRepository<Contrat, Long> {}
