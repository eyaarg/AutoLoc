package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IPaiementRepository extends JpaRepository<Paiement,Long> {
}
