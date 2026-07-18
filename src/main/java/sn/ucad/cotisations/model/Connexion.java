package sn.ucad.cotisations.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Trace d'une connexion réussie d'un membre (historique des connexions).
 */
@Entity
@Table(name = "connexion")
public class Connexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id", nullable = false)
    private Membre membre;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Column(name = "adresse_ip", length = 60)
    private String adresseIp;

    public Connexion() {}

    public Connexion(Membre membre, String adresseIp) {
        this.membre    = membre;
        this.adresseIp = adresseIp;
        this.dateHeure = LocalDateTime.now();
    }

    public int getId()                 { return id; }
    public void setId(int id)          { this.id = id; }

    public Membre getMembre()              { return membre; }
    public void setMembre(Membre membre)   { this.membre = membre; }

    public LocalDateTime getDateHeure()               { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public String getAdresseIp()              { return adresseIp; }
    public void setAdresseIp(String adresseIp){ this.adresseIp = adresseIp; }

    // Date/heure formatée pour l'affichage (jj/MM/aaaa HH:mm)
    @Transient
    public String getDateHeureFormatee() {
        if (dateHeure == null) return "";
        return dateHeure.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
