package esprit.fx.autoloc;

import esprit.fx.autoloc.Entities.Contrat;
import esprit.fx.autoloc.Entities.ModePaiement;
import esprit.fx.autoloc.Entities.Paiement;
import esprit.fx.autoloc.Repositories.ContratRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;


@SpringBootApplication
public class AutoLocApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoLocApplication.class, args);
    }

}
