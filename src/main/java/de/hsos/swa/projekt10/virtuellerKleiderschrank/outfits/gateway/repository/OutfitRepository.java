package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;

public class OutfitRepository implements OutfitKatalog{

    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public long erstelleOutfitFuerEinenBenutzer(OutfitInputDTO dto, String benutzername) {
        Outfit outfit = new Outfit(dto.name, dto.kategorien, false, benutzername);
        this.entityManager.persist(outfit);
        return outfit.getOutfitId();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, long outfitId, String benutzername) {
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            return false;
        }
        if(dto.name != null){
            outfit.setName(dto.name);
        }
        outfit.setBenutzername(benutzername);
        outfit.setKategorien(dto.kategorien);
        return false;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername) {
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            return false;
        }
        this.entityManager.remove(outfit);
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername) {
        List<Outfit> zuLoeschen = this.gebeAlleOutfitsVomBenutzer(benutzername);
        for(int index = 0; index < zuLoeschen.size(); index++){
            this.entityManager.remove(zuLoeschen.get(index));
        }
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername) {
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            return null;
        }
        outfit.getKleidungsstuecke().add(kleidungsId);
        this.entityManager.persist(outfit);
        return outfit;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername) {
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            return false;
        }
        outfit.getKleidungsstuecke().remove(kleidungsId);
        this.entityManager.persist(outfit);
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Outfit> gebeAlleOutfitsVomBenutzer(String benutzername) {
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        
        return query.getResultList().stream().filter(outfit -> {
            return outfit.getBenutzername().equals(benutzername);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit gebeOutfitVomBenutzerMitId(long outfitId, String benutzername) {
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        
        return query.getResultList().stream().filter(outfit -> {
            return outfit.getBenutzername().equals(benutzername) && outfit.getOutfitId ()== outfitId;
        }).findFirst().get();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Outfit> gebeAlleOutfitsVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        
        return query.getResultList().stream().filter(outfit -> {
            return outfit.getBenutzername().equals(benutzername) && outfit.besitztOutfitKategorie(kategorie);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit gebeGeteilitesOutfit(long outfitId) {
        return this.entityManager.find(Outfit.class, outfitId);
    }
    
}
