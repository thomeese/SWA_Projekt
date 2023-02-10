package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import io.quarkus.arc.log.LoggerName;

@Dependent
public class KleidungsstueckRepository implements KleidungsstueckKatalog{

    @LoggerName("kl-repo")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckRepository.class);


    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - gestartet");
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidungsstueck == null){
            kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
            return false;
        }
        try{
            this.entityManager.remove(kleidungsstueck);
        }catch(IllegalArgumentException | TransactionRequiredException e){
            kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
            return false;
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet");
        return true;    
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - gestartet");
        List<Kleidungsstueck> zuLoeschen = this.gebeAlleKleidungsstueckeVomBenutzer(benutzername);
        try{
            for(int index = 0; index < zuLoeschen.size(); index++){
                this.entityManager.remove(zuLoeschen.get(index));
            }
        }catch(IllegalArgumentException | TransactionRequiredException e){
            kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - beendet ohne das alle Kleidungsstuecke geloescht wurde");
            return false;
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - beendet");
        return true;
    }
    
    @Override
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - gestartet");
        try{
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername and k.kleidungsId = :kleidungsId";
            TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername",benutzername);
            query.setParameter("kleidungsId",kleidungsId);
            Kleidungsstueck erg = query.getSingleResult();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - holt ein Kleidungsstueck nach Id fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - beendet");
            return erg;
        }catch(IllegalStateException | PersistenceException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - holt ein Kleidungsstueck nach Id fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - beendet ohne das ein Kleidungsstueck gefunden wurde");
            return null;
        }
        
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinesTyp(Typ typ, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - gestartet");
        try{
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername and k.typ = :typ";
            TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername",benutzername);
            query.setParameter("typ",typ);
            List<Kleidungsstueck> kleidungsList = query.getResultList();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - holt alle Kleidungsstuecke eines Typs fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - beendet");
            return kleidungsList;
        }catch(IllegalStateException | TransactionRequiredException | QueryTimeoutException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - holt alle Kleidungsstuecke eines Typs fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }
    }


    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerMitNamen(String name, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - gestartet");
        try{
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername and k.name = :name";
            TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername",benutzername);
            query.setParameter("name",name);
            List<Kleidungsstueck> kleidungsList = query.getResultList();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - holt alle Kleidungsstuecke nach Namen/Marke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - beendet");
        return kleidungsList;
        }catch(IllegalStateException | TransactionRequiredException | QueryTimeoutException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - holt alle Kleidungsstuecke nach Namen/Marke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(Farbe farbe, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - gestartet");
        try{
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername and k.farbe = :farbe";
            TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername",benutzername);
            query.setParameter("farbe",farbe);
            List<Kleidungsstueck> kleidungsList = query.getResultList();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - holt alle Kleidungsstuecke einer Farbe fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - beendet");
            return kleidungsList;
        }catch(IllegalStateException | TransactionRequiredException | QueryTimeoutException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - holt alle Kleidungsstuecke einer Farbe fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }
    }


    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        try{
            List<Kleidungsstueck> kleidungsList = query.getResultList().stream().filter(kleidung -> {
                return kleidung.getBenutzername().equals(benutzername) && kleidung.besitztKleidungsstueckKategorie(kategorie);
            }).toList();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - holt alle Kleidungsstuecke einer Kategorie fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - beendet");
            return kleidungsList;
        }catch(IllegalStateException | TransactionRequiredException | QueryTimeoutException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - holt alle Kleidungsstuecke einer Kategorie fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }
        
    }

    @Override
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - gestartet");
        try{
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername";
            TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername", benutzername);
            List<Kleidungsstueck> kleidungsList = query.getResultList();
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - holt alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - beendet");
            return kleidungsList;
        }catch(IllegalStateException | TransactionRequiredException | QueryTimeoutException e){
            kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - holt alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - gestartet");
        Kleidungsstueck kleidung = new Kleidungsstueck(dto.groesse, dto.farbe, dto.typ, dto.name, dto.kategorien, benutzername);
        this.entityManager.persist(kleidung);
        kleidungLog.trace(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - erstellt ein neues Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - beendet");
        return kleidung.getKleidungsId();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto,
            String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - gestartet");
        Kleidungsstueck kleidung = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidung == null){
            kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck bearbeitet wurde");
            return false;
        }
        if(dto.groesse != null){
            kleidung.setGroesse(dto.groesse);
        }
        if(dto.farbe != null){
            kleidung.setFarbe(dto.farbe);
        }
        if(dto.typ != null){
            kleidung.setTyp(dto.typ);
        }
        kleidung.setBenutzername(benutzername);
        kleidung.setKategorien(dto.kategorien);
        this.entityManager.persist(kleidung);
        kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers - beendet");
        return true;
    }

    
}
