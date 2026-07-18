package sn.ucad.cotisations.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cotisation")
public class Cotisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @Column(name = "mois", nullable = false)
    private int mois;

    @Column(name = "annee", nullable = false)
    private int annee;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.EN_ATTENTE;

    public enum Statut { PAYE, EN_ATTENTE, EN_RETARD }

    // Constructeurs
    public Cotisation() {}

    public Cotisation(Membre membre, BigDecimal montant,
                      int mois, int annee, String modePaiement) {
        this.membre       = membre;
        this.montant      = montant;
        this.mois         = mois;
        this.annee        = annee;
        this.modePaiement = modePaiement;
        this.datePaiement = LocalDate.now();
    }

    // Getters & Setters
    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public Membre getMembre()                { return membre; }
    public void setMembre(Membre membre)     { this.membre = membre; }

    public BigDecimal getMontant()               { return montant; }
    public void setMontant(BigDecimal montant)   { this.montant = montant; }

    public LocalDate getDatePaiement()                   { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement)  { this.datePaiement = datePaiement; }

    public int getMois()            { return mois; }
    public void setMois(int mois)   { this.mois = mois; }

    public int getAnnee()             { return annee; }
    public void setAnnee(int annee)   { this.annee = annee; }

    public String getModePaiement()                  { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public Statut getStatut()              { return statut; }
    public void setStatut(Statut statut)   { this.statut = statut; }

    // Utilitaire : nom du mois en français
    public String getNomMois() {
        String[] mois = {"","Janvier","Février","Mars","Avril","Mai","Juin",
                         "Juillet","Août","Septembre","Octobre","Novembre","Décembre"};
        return mois[this.mois];
    }
}