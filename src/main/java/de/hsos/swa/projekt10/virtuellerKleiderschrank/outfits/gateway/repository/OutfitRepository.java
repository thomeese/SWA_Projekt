package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.events.AlleKleidungsstueckeEntfernt;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.events.KleidungsstueckEntfernt;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitTeilenDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import io.quarkus.arc.log.LoggerName;

@Dependent
/**
 * Repositorie fuer das Entity Outfit. 
 * Enthaelt alle Methode die fuer die Interaktion mit
 * dem Entity in Kombination mit der Datenbak benoetigt wird
 * @author Thomas Meese
 */
public class OutfitRepository implements OutfitKatalog {
    @LoggerName("out-repo")
    private static Logger outfitLog = Logger.getLogger(OutfitRepository.class);

    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public long erstelleOutfitFuerEinenBenutzer(OutfitInputDTO dto, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": erstelleOutfitFuerEinenBenutzer-Methode - gestartet");
        Outfit outfit = new Outfit(dto.name, dto.kategorien, false, benutzername);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis()
                + ": erstelleOutfitFuerEinenBenutzer-Methode - erstellt ein neues Outfit fuer einen Benutzer durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": erstelleOutfitFuerEinenBenutzer-Methode - beendet");
        return outfit.getOutfitId();
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if (outfit == null) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": bearbeiteOutfitEinesBenutzers-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": bearbeiteOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit bearbeitet wurde");
            return false;
        }
        if (dto.name != null) {
            outfit.setName(dto.name);
        }
        outfit.setBenutzername(benutzername);
        outfit.setKategorien(dto.kategorien);
        outfit.setKleidungsstuecke(dto.kleidungsstuecke);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis()
                + ": bearbeiteOutfitEinesBenutzers-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfitEinesBenutzers-Methode - beendet");
        return true;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if (outfit == null) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": loescheOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit geloscht wurde");
            return false;
        }
        try {
            this.entityManager.remove(outfit);
            outfitLog.trace(System.currentTimeMillis()
                    + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfitEinesBenutzers-Methode - beendet");
            return true;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": loescheOutfitEinesBenutzers-Methode - loescht ein Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": loescheOutfitEinesBenutzers-Methode - beendet ohne das ein Outfit geloscht wurde");
            return false;
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - gestartet");
        List<Outfit> zuLoeschen = this.gebeAlleOutfitsVomBenutzer(new OutfitFilter(), benutzername);
        try {
            for (int index = 0; index < zuLoeschen.size(); index++) {
                this.entityManager.remove(zuLoeschen.get(index));
            }
            outfitLog.trace(System.currentTimeMillis()
                    + ": loescheAlleOutfitsEinesBenutzers-Methode - loescht alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfitsEinesBenutzers-Methode - beendet");
            return true;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": loescheAlleOutfitsEinesBenutzers-Methode - loescht alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": loescheAlleOutfitsEinesBenutzers-Methode - beendet ohne das alle Outfits geloscht wurde");
            return false;
        }

    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if (outfit == null) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - fuegt ein Kleidungsstueck einem Outfit fuer einen Benutzer hinzu durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - beendet ohne das ein Kleidungsstueck hinzugefuegt wurde");
            return null;
        }
        outfit.getKleidungsstuecke().add(kleidungsId);
        this.entityManager.persist(outfit);
        outfitLog.trace(System.currentTimeMillis()
                + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - fuegt ein Kleidungsstueck einem Outfit fuer einen Benutzer hinzu durch Repository");
        outfitLog.debug(System.currentTimeMillis() + ": fuegeKleidungsstueckZuOutfitHinzu-Methode - beendet");
        return outfit;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if (outfit == null) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": entferneKleidungsstueckVomOutfit-Methode - beendet ohne das ein Kleidungsstueck entfernt wurde");
            return false;
        }
        try {
            outfit.getKleidungsstuecke().remove(kleidungsId);
            this.entityManager.persist(outfit);
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueckVomOutfit-Methode - beendet");
            return true;
        } catch (ClassCastException | NullPointerException | UnsupportedOperationException
                | EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneKleidungsstueckVomOutfit-Methode - entfernt ein Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": entferneKleidungsstueckVomOutfit-Methode - beendet ohne das ein Kleidungsstueck entfernt wurde");
            return false;
        }
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    /**
     * Enternt alle Kleidungsstuecke aus einem Outfit eines Benutzers.
     * Die Aenderung wird anschliessend in der Datenbank gespeichert.
     * @param outfitId Id des Outfits dessen Kleidungsstuecke entfernt werden sollen
     * @param benutzername Benutzer dessen Outfit bearbeitet werden soll
     * @return boolean true bei erfolgt, false bei misserfolg
     * 
     * @author Thomas Meese
     */
    public boolean entferneAlleKleidungsstueckeVomOutfit(long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": entferneAlleKleidungsstueckeVomOutfit-Methode - gestartet");
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if (outfit == null) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneAlleKleidungsstueckeVomOutfit-Methode - entfernt alle Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": entferneAlleKleidungsstueckeVomOutfit-Methode - beendet ohne das ein Kleidungsstueck entfernt wurde");
            return false;
        }
        outfit.setKleidungsstuecke(new ArrayList<Long>());
        try {
            this.entityManager.persist(outfit);
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneAlleKleidungsstueckeVomOutfit-Methode - entfernt alle Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": entferneAlleKleidungsstueckeVomOutfit-Methode - beendet");
            return true;
        } catch (ClassCastException | NullPointerException | UnsupportedOperationException
                | EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": entferneAlleKleidungsstueckeVomOutfit-Methode - entfernt alle Kleidungsstueck aus einem Outfit fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": entferneAlleKleidungsstueckeVomOutfit-Methode - beendet ohne das alle Kleidungsstuecke entfernt wurden");
            return false;
        }
    }

    @Override
    public List<Outfit> gebeAlleOutfitsVomBenutzer(OutfitFilter filter, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - gestartet");
        try {
            String querryString = "select o from Outfit o where o.benutzername = :benutzername";
            if (filter.name != null && !filter.name.equals("")) {
                querryString = querryString + " and o.name = :name";
            }
            TypedQuery<Outfit> query = this.entityManager.createQuery(querryString, Outfit.class);
            query.setParameter("benutzername", benutzername);
            if (filter.name != null && !filter.name.equals("")) {
                query.setParameter("name", filter.name);
            }
            List<Outfit> outfits;
            if (filter.kategorie != null && !filter.kategorie.equals("")) {
                outfits = query.getResultList().stream().filter(outfit -> {
                    return outfit.getBenutzername().equals(benutzername)
                            && outfit.besitztOutfitKategorie(filter.kategorie);
                }).toList();
            } else {
                outfits = query.getResultList();
            }
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeAlleOutfitsVomBenutzer-Methode - holt alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeAlleOutfitsVomBenutzer-Methode - beendet");
            return outfits;
        } catch (IllegalStateException | PersistenceException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeAlleOutfitsVomBenutzer-Methode - holt alle Outfits fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": gebeAlleOutfitsVomBenutzer-Methode - beendet ohne das Outfits gefunden wurden");
            return new ArrayList<Outfit>();
        }

    }

    @Override
    public Outfit gebeOutfitVomBenutzerMitId(long outfitId, String benutzername) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - gestartet");
        try {
            String querryString = "select o from Outfit o where o.benutzername = :benutzername and o.outfitId = :outfitId";
            TypedQuery<Outfit> query = this.entityManager.createQuery(querryString, Outfit.class);
            query.setParameter("benutzername", benutzername);
            query.setParameter("outfitId", outfitId);
            Outfit outfit = query.getSingleResult();
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeOutfitVomBenutzerMitId-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeOutfitVomBenutzerMitId-Methode - beendet");
            return outfit;
        } catch (IllegalStateException | PersistenceException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeOutfitVomBenutzerMitId-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": gebeOutfitVomBenutzerMitId-Methode - beendet ohne das ein Outfit gefunden wurde");
            return null;
        }

    }

    @Override
    public Outfit gebeGeteilitesOutfit(long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": gebeGeteilitesOutfit-Methode - gestartet");
        try {
            String querryString = "select o from Outfit o where o.outfitId = :outfitId and o.geteilt = :geteilt";
            TypedQuery<Outfit> query = this.entityManager.createQuery(querryString, Outfit.class);
            query.setParameter("outfitId", outfitId);
            query.setParameter("geteilt", true);
            Outfit outfit = query.getSingleResult();
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeGeteilitesOutfit-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis() + ": gebeGeteilitesOutfit-Methode - beendet");
            return outfit;
        } catch (IllegalStateException | PersistenceException e) {
            outfitLog.trace(System.currentTimeMillis()
                    + ": gebeGeteilitesOutfit-Methode - holt ein Outfit nach Id fuer einen Benutzer durch Repository");
            outfitLog.debug(System.currentTimeMillis()
                    + ": gebeGeteilitesOutfit-Methode - beendet ohne das ein Outfit gefunden wurde");
            return null;
        }
    }

    /**
     * Die Methode behandelt das KleidungstueckEntfernt-Event.
     * Bei der Behandlung werden alle Outfits des Benutzers
     * durchsucht, wenn ein Outfit die KleidungsstueckId besitzt
     * wird diese aus dem jeweiligen Outfit entfernt.
     * 
     * @param event zu behandelndes Event
     * @auhtor Thomas Meese
     */
    public void behandleKleidungsstueckEntfernt(@ObservesAsync KleidungsstueckEntfernt event) {
        List<Outfit> outfits = this.gebeAlleOutfitsVomBenutzer(new OutfitFilter(), event.benutzername());
        for (int index = 0; index < outfits.size(); index++) {
            for (int index2 = 0; index2 < outfits.get(index).getKleidungsstuecke().size(); index2++) {
                if (outfits.get(index).getKleidungsstuecke().get(index2) == event.klediungsId()) {
                    this.entferneKleidungsstueckVomOutfit(event.klediungsId(), outfits.get(index).getOutfitId(),
                            event.benutzername());
                }
            }

        }
    }

    /**
     * Die Methode behandelt das AlleKleidungstueckEntfernt-Event.
     * Bei der Behandlung werden aus allen Outfits des Benutzers
     * alle KleidungsstueckIds entfernt.
     * 
     * @param event zu behandelndes Event
     * @auhtor Thomas Meese
     */
    public void behandleAlleKleidungsstueckeEntfernt(@ObservesAsync AlleKleidungsstueckeEntfernt event) {
        List<Outfit> outfits = this.gebeAlleOutfitsVomBenutzer(new OutfitFilter(), event.benutzername());
        for (int index = 0; index < outfits.size(); index++) {
            this.entferneAlleKleidungsstueckeVomOutfit(outfits.get(index).getOutfitId(), event.benutzername());
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean teileOutfitEinesBenutzers(long outfitId, OutfitTeilenDTO dto, String benutzername) {
        Outfit outfit = this.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
        if(outfit == null){
            return false;
        }
        outfit.setGeteilt(dto.teilen);
        this.entityManager.persist(outfit);
        return true;
    }

}
