package sn.ucad.cotisations.service;

import sn.ucad.cotisations.dao.AmendeDAO;
import sn.ucad.cotisations.dao.MembreDAO;
import sn.ucad.cotisations.model.Amende;
import sn.ucad.cotisations.model.Membre;

import java.math.BigDecimal;
import java.util.List;

public class AmendeService {

    private final AmendeDAO amendeDAO = new AmendeDAO();
    private final MembreDAO membreDAO = new MembreDAO();

    public void payerAmende(int amendeId) {
        Amende amende = amendeDAO.trouverParId(amendeId);
        if (amende == null) throw new IllegalArgumentException("Amende introuvable.");

        if (amende.getStatutPaiement() == Amende.StatutPaiement.PAYE) {
            throw new IllegalArgumentException("Cette amende est déjà payée.");
        }

        amende.setStatutPaiement(Amende.StatutPaiement.PAYE);
        amendeDAO.modifier(amende);
    }

    public void ajouterAmendeManuellement(int membreId, BigDecimal montant) {
        Membre membre = membreDAO.trouverParId(membreId);
        if (membre == null) throw new IllegalArgumentException("Membre introuvable.");

        Amende amende = new Amende(membre, montant);
        amendeDAO.ajouter(amende);
    }

    public List<Amende> trouverParMembre(int membreId) {
        return amendeDAO.trouverParMembre(membreId);
    }

    public List<Amende> trouverToutes() {
        return amendeDAO.trouverToutes();
    }

    public List<Amende> trouverNonPayees(int membreId) {
        return amendeDAO.trouverNonPayees(membreId);
    }

    public Amende trouverParId(int id) {
        return amendeDAO.trouverParId(id);
    }

    public BigDecimal totalAmendesNonPayees(int membreId) {
        return trouverNonPayees(membreId).stream()
            .map(Amende::getMontant)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}