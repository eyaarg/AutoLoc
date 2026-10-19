package esprit.fx.autoloc.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    @Enumerated(EnumType.STRING)
    private StatutReservation statut;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    @OneToOne(fetch = FetchType.LAZY)
    private Vehicule vehicule;
    //REPOSITORIES POUR AUTOLOC
}
