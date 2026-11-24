package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IEquipementRepository extends JpaRepository<Equipement,Long> {
}
