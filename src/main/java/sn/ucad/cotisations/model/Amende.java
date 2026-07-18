package sn.ucad.cotisations.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "amende")
public class Amende {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Column(name = "date_generation", nullable = false)
    private LocalDate dateGeneration;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement")
    private StatutPaiement statutPaiement = StatutPaiement.NON_PAYE;

    public enum StatutPaiement { PAYE, NON_PAYE }

    // Constructeurs
    public Amende() {}

    public Amende(Membre membre, BigDecimal montant) {
        this.membre          = membre;
        this.montant         = montant;
        this.dateGeneration  = LocalDate.now();
    }

    // Getters & Setters
    public int getId()           { return id; }
    public void setId(int id)    { this.id = id; }

    public Membre getMembre()              { return membre; }
    public void setMembre(Membre membre)   { this.membre = membre; }

    public BigDecimal getMontant()             { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public LocalDate getDateGeneration()                     { return dateGeneration; }
    public void setDateGeneration(LocalDate dateGeneration)  { this.dateGeneration = dateGeneration; }

    public StatutPaiement getStatutPaiement()                    { return statutPaiement; }
    public void setStatutPaiement(StatutPaiement statutPaiement) { this.statutPaiement = statutPaiement; }
}