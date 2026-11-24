package esprit.fx.autoloc;

import esprit.fx.autoloc.Entities.Contrat;
import esprit.fx.autoloc.Entities.ModePaiement;
import esprit.fx.autoloc.Entities.Paiement;
import esprit.fx.autoloc.Repositories.IContratRepository;
import esprit.fx.autoloc.Repositories.IPaiementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j // Lombok crée l'objet "log" : log.info("texte {}", valeur)
@SpringBootApplication
public class AutoLocApplication {

    // Fuseau fixe : la date ne dépend plus de la machine
    private static final ZoneId FUSEAU = ZoneId.of("Africa/Tunis");

    public static void main(String[] args) {
        SpringApplication.run(AutoLocApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(IContratRepository contratRepo, IPaiementRepository paiementRepo) {
        return args -> {

            // ===== 1. CREATE avec save() : id null, donc INSERT =====
            Contrat contrat = new Contrat();
            contrat.setDateSignature(LocalDate.now(FUSEAU));
            contrat.setMontantTotal(new BigDecimal("500.00"));
            contrat.setValide(true);

            Paiement p1 = new Paiement();
            p1.setMontant(new BigDecimal("200.00"));
            p1.setDatePaiement(LocalDate.now(FUSEAU));
            p1.setModePaiement(ModePaiement.CARTE);

            Paiement p2 = new Paiement();
            p2.setMontant(new BigDecimal("300.00"));
            p2.setDatePaiement(LocalDate.now(FUSEAU));
            p2.setModePaiement(ModePaiement.ESPECES);

            // addPaiement relie les deux côtés (liste du contrat + p.contrat)
            contrat.addPaiement(p1);
            contrat.addPaiement(p2);

            // save(entité) : retourne l'entité enregistrée, avec son id rempli par MySQL.
            // Grâce à cascade = ALL, les 2 paiements sont insérés aussi.
            Contrat saved = contratRepo.save(contrat);
            log.info(">>> CREATE : contrat id = {}", saved.getIdContrat());
            // Logs SQL attendus : insert into contrat, puis insert into paiement (x2)

            // ===== 2. READ avec findById() : retourne un Optional (jamais null) =====
            Optional<Contrat> trouve = contratRepo.findById(saved.getIdContrat());
            log.info(">>> READ id existant : présent ? {}", trouve.isPresent());
            // Log SQL attendu : select ... from contrat where id_contrat=?

            // Id inexistant : Optional vide, aucune exception
            log.info(">>> READ id 9999 : présent ? {}", contratRepo.findById(9999L).isPresent());

            // ===== 3. UPDATE avec save() : id renseigné, donc SELECT puis UPDATE =====
            Contrat aModifier = trouve.orElseThrow();   // lève une exception si l'Optional est vide
            aModifier.setValide(false);
            Contrat modifie = contratRepo.save(aModifier); // on utilise l'objet retourné
            log.info(">>> UPDATE : valide = {}", modifie.isValide());
            // Logs SQL attendus : select ... puis update contrat set ... where id_contrat=?

            // ===== 4. Autres méthodes de JpaRepository =====
            log.info(">>> existsById : {}", contratRepo.existsById(saved.getIdContrat())); // true/false
            log.info(">>> count : {}", contratRepo.count());                               // nombre de lignes
            List<Contrat> tous = contratRepo.findAll();      // JpaRepository renvoie une List
            log.info(">>> findAll : {} contrat(s)", tous.size());

            // saveAndFlush : save + envoi immédiat du SQL à la base
            modifie.setValide(true);
            contratRepo.saveAndFlush(modifie);

            // ===== 5. TRI et PAGINATION =====
            // Sort.by prend le nom de l'attribut JAVA ("montantTotal"), pas de la colonne SQL
            Sort tri = Sort.by("montantTotal").descending();
            List<Contrat> tries = contratRepo.findAll(tri);       // order by montant_total desc
            log.info(">>> TRI : {} contrat(s) triés", tries.size());
            // PageRequest.of(numéro de page à partir de 0, taille, tri)
            Page<Contrat> page = contratRepo.findAll(PageRequest.of(0, 2, tri));
            log.info(">>> PAGE : {} élément(s), total = {}, pages = {}",
                    page.getContent().size(), page.getTotalElements(), page.getTotalPages());
            // Logs SQL attendus : select ... limit ? puis select count(*)

            // ===== 6. DELETE avec deleteById() : cascade + orphanRemoval =====
            long avant = paiementRepo.count();
            contratRepo.deleteById(saved.getIdContrat());
            // Les paiements du contrat sont supprimés avec lui (cascade REMOVE)
            log.info(">>> DELETE : paiements avant = {}, après = {}", avant, paiementRepo.count());
            // Logs SQL attendus : delete from paiement (x2), puis delete from contrat

            // deleteById sur un id inexistant : ne lève pas d'exception (Spring Data 3)
            contratRepo.deleteById(9999L);
            log.info(">>> deleteById(9999) : aucune exception");
        };
    }
}