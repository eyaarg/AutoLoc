package esprit.fx.autoloc.Services;

import esprit.fx.autoloc.Entities.Contrat;
import java.util.List;

public interface IContratService {
    Contrat create(Contrat contrat);
    Contrat findById(Long id);
    List<Contrat> findAll();
    Contrat update(Long id, Contrat contrat);
    void deleteById(Long id);
}
