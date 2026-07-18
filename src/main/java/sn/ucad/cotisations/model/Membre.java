package sn.ucad.cotisations.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "membre")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "numero", nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "prenom", nullable = false, length = 50)
    private String prenom;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_adhesion", nullable = false)
    private LocalDate dateAdhesion;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.MEMBRE;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut = Statut.ACTIF;

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cotisation> cotisations;

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Amende> amendes;

    // Enums
    public enum Role   { ADMIN, MEMBRE }
    public enum Statut { ACTIF, INACTIF }

    // Constructeurs
    public Membre() {}

    public Membre(String numero, String prenom, String nom, String email,
                  String password, LocalDate dateAdhesion) {
        this.numero      = numero;
        this.prenom      = prenom;
        this.nom         = nom;
        this.email       = email;
        this.password    = password;
        this.dateAdhesion = dateAdhesion;
    }

    // Getters & Setters
    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public String getNumero()            { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getPrenom()            { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom()               { return nom; }
    public void setNom(String nom)       { this.nom = nom; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDateNaissance()                  { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance){ this.dateNaissance = dateNaissance; }

    public LocalDate getDateAdhesion()                   { return dateAdhesion; }
    public void setDateAdhesion(LocalDate dateAdhesion)  { this.dateAdhesion = dateAdhesion; }

    public Role getRole()                { return role; }
    public void setRole(Role role)       { this.role = role; }

    public Statut getStatut()            { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public List<Cotisation> getCotisations()               { return cotisations; }
    public void setCotisations(List<Cotisation> cotisations){ this.cotisations = cotisations; }

    public List<Amende> getAmendes()           { return amendes; }
    public void setAmendes(List<Amende> amendes){ this.amendes = amendes; }

    // Méthode utilitaire
    public String getNomComplet() { return prenom + " " + nom; }
}