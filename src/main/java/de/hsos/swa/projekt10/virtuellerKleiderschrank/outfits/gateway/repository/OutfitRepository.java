package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import io.quarkus.arc.log.LoggerName;

@Dependent
public class OutfitRepository implements OutfitKatalog{
    @LoggerName("out-repo")
    private static Logger outfitLog = Logger.getLogger(OutfitRepository.class);
    
    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public long erstelleOutfitFuerEinenBenutzer(OutfitInputDTO dto, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": erstelleOutfitFuerEinenBenutzer-Methode - gestartet");
        Outfit outfit = new Outfit(dto.name, dto.kategorien, false, benutzername);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis() + ": erstelleOutfitFuerEinenBenutzer-Methode - erstellt ein neues Outfit fuer einen Benutzer durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": erstelleOutfitFuerEinenBenutzer-Methode - beendet");
        return outfit.getOutfitId();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit bearbeitet wurde");
            return false;
        }
        if(dto.name != null){
            outfit.setName(dto.name);
        }
        outfit.setBenutzername(benutzername);
        outfit.setKategorien(dto.kategorien);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - beendet");
        return false;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            outfitLog.trace(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit geloscht wurde");
            return false;
        }
        try{
            this.entityManager.remove(outfit);
            outfitLog.trace(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - beendet");
        return true;
        }catch(IllegalArgumentException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit geloscht wurde");
            return false;
        }
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - gestartet");
        List<Outfit> zuLoeschen = this.gebeAlleOutfitsVomBenutzer(benutzername);
        try{
            for(int index = 0; index < zuLoeschen.size(); index++){
                this.entityManager.remove(zuLoeschen.get(index));
            }
            outfitLog.trace(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - loescht alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - beendet");
            return true;
        }catch(IllegalArgumentException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - loescht alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - beendet ohne das alle Outfits geloscht wurde");
            return false;
        }
        
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            outfitLog.trace(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - fuegt ein Kleidungsstueck einem Outfit fuer einen Benutzer hinzu durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - beendet ohne das ein Kleidungsstueck hinzugefuegt wurde");
            return null;
        }
        outfit.getKleidungsstuecke().add(kleidungsId);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - fuegt ein Kleidungsstueck einem Outfit fuer einen Benutzer hinzu durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - beendet");
        return outfit;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            outfitLog.trace(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - beendet ohne das ein Kleidungsstueck entfernt wurde");
            return false;
        }
        try{
            outfit.getKleidungsstuecke().remove(kleidungsId);
            this.entityManager.persist(outfit);
            outfitLog.trace(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - beendet");
        return true;
        }catch(ClassCastException | NullPointerException | UnsupportedOperationException 
        |EntityExistsException | IllegalArgumentException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - beendet ohne das ein Kleidungsstueck entfernt wurde");
            return false;
        }
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Outfit> gebeAlleOutfitsVomBenutzer(String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - gestartet");
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        try{
            List<Outfit> outfitList = query.getResultList().stream().filter(outfit -> {
                return outfit.getBenutzername().equals(benutzername);
            }).toList();
            outfitLog.trace(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - holt alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - beendet");
            return outfitList;
        }catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - holt alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - beendet ohne das Outfits gefunden wurden");
            return new ArrayList<Outfit>();
        }
       
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit gebeOutfitVomBenutzerMitId(long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - gestartet");
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        try{
            Outfit erg = query.getResultList().stream().filter(outfit -> {
                return outfit.getBenutzername().equals(benutzername) && outfit.getOutfitId ()== outfitId;
            }).findFirst().get();
            outfitLog.trace(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - beendet");
            return erg;
        }catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - beendet ohne das ein Outfit gefunden wurde");
            return null; 
        }
        
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Outfit> gebeAlleOutfitsVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzerEinerKategorie-Methode - gestartet");
        TypedQuery<Outfit> query= this.entityManager.createQuery("select o from Outfit o", Outfit.class);
        try{
            List<Outfit> outfitList = query.getResultList().stream().filter(outfit -> {
                return outfit.getBenutzername().equals(benutzername) && outfit.besitztOutfitKategorie(kategorie);
            }).toList();
            outfitLog.trace(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzerEinerKategorie-Methode - holt alle Outfits einer Kategorie fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzerEinerKategorie-Methode - beendet");
            return outfitList; 
        }catch(IllegalStateException | QueryTimeoutException | TransactionRequiredException e){
            outfitLog.trace(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzerEinerKategorie-Methode - holt alle Outfits einer Kategorie fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzerEinerKategorie-Methode - beendet ohne das Outfits gefunden wurden");
            return new ArrayList<Outfit>();
        }
        
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Outfit gebeGeteilitesOutfit(long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeGeteilitesOutfit-Methode - gestartet");
        Outfit outfit = this.entityManager.find(Outfit.class, outfitId);
        outfitLog.trace(System.currentTimeMillis() + ": gebeGeteilitesOutfit-Methode - holt ein geteiltes Outfit nach Id durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": gebeGeteilitesOutfit-Methode - beendet");
        return outfit;
    }
    
}
