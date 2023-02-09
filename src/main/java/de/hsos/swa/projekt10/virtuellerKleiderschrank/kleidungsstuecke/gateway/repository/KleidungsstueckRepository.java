package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet");
            return false;
        }
        this.entityManager.remove(kleidungsstueck);
        kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
        return true;    
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - gestartet");
        List<Kleidungsstueck> zuLoeschen = this.gebeAlleKleidungsstueckeVomBenutzer(benutzername);
        for(int index = 0; index < zuLoeschen.size(); index++){
            this.entityManager.remove(zuLoeschen.get(index));
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - holt ein Kleidungsstueck nach Id fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getKleidungsId() == kleidungsId;
        }).findFirst().get();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinesTyp(Typ typ, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - holt alle Kleidungsstuecke eines Typs fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinesTyp-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getTyp() == typ;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerMitNamen(String name, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - holt alle Kleidungsstuecke nach Namen/Marke fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerMitNamen-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getName().equals(name);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(Farbe farbe, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - holt alle Kleidungsstuecke einer Farbe fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerFarbe-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getFarbe() == farbe;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - holt alle Kleidungsstuecke einer Kategorie fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzerEinerKategorie-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.besitztKleidungsstueckKategorie(kategorie);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - gestartet");
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        kleidungLog.trace(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - holt alle Kleidungsstuecke fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - beendet");
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - gestartet");
        Kleidungsstueck kleidung = new Kleidungsstueck(dto.groesse, dto.farbe, dto.type, dto.name, dto.kategorien, benutzername);
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
        if(dto.type != null){
            kleidung.setTyp(dto.type);
        }
        kleidung.setBenutzername(benutzername);
        kleidung.setKategorien(dto.kategorien);
        kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers - beendet");
        return true;
    }

    
}
