package esprit.fx.autoloc.Entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contrat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;
    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();
    public void addPaiement(Paiement p) {
        paiements.add(p);
        p.setContrat(this);
    }

    public void removePaiement(Paiement p) {
        paiements.remove(p);
        p.setContrat(null);
    }
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;
}
