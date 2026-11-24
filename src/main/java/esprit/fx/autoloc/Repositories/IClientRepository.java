package esprit.fx.autoloc.Repositories;

import esprit.fx.autoloc.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface IClientRepository extends JpaRepository<Client,Long> {
}
