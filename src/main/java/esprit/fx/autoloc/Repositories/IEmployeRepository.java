package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IEmployeRepository extends JpaRepository<Employe,Long> {
}
