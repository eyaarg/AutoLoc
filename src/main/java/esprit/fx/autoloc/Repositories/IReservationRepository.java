package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IReservationRepository extends JpaRepository<Reservation,Long> {
}
