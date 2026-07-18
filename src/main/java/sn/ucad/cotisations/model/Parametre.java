package sn.ucad.cotisations.model;

import jakarta.persistence.*;

/**
 * Paramètre de configuration de l'application (stockage clé/valeur).
 * Ex : montant de la cotisation mensuelle, montant de l'amende.
 */
@Entity
@Table(name = "parametre")
public class Parametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cle", nullable = false, unique = true, length = 60)
    private String cle;

    @Column(name = "valeur", nullable = false, length = 255)
    private String valeur;

    @Column(name = "libelle", length = 150)
    private String libelle;

    public Parametre() {}

    public Parametre(String cle, String valeur, String libelle) {
        this.cle     = cle;
        this.valeur  = valeur;
        this.libelle = libelle;
    }

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public String getCle()             { return cle; }
    public void setCle(String cle)     { this.cle = cle; }

    public String getValeur()              { return valeur; }
    public void setValeur(String valeur)   { this.valeur = valeur; }

    public String getLibelle()               { return libelle; }
    public void setLibelle(String libelle)   { this.libelle = libelle; }
}
