package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IMaintenanceRepository extends JpaRepository<Maintenance,Long> {
}
