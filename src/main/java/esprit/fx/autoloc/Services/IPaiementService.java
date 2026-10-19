package esprit.fx.autoloc.Services;

import esprit.fx.autoloc.Entities.Paiement;
import java.util.List;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();
}