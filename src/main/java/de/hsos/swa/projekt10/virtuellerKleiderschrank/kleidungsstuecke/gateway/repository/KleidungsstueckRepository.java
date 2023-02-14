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

import javax.enterprise.event.Event;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.events.KleidungsstueckEntfernt;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.events.AlleKleidungsstueckeEntfernt;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
import io.quarkus.arc.log.LoggerName;

@Dependent
public class KleidungsstueckRepository implements KleidungsstueckKatalog {

    @LoggerName("kl-repo")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckRepository.class);

    @Inject
    private EntityManager entityManager;

    @Inject
    Event<KleidungsstueckEntfernt> kleidungsstueckEntferntEvent;

    @Inject
    Event<AlleKleidungsstueckeEntfernt> alleKleidungsstueckEntferntEvent;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - gestartet");
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if (kleidungsstueck == null) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
            return false;
        }
        try {
            this.entityManager.remove(kleidungsstueck);
            this.kleidungsstueckEntferntEvent.fireAsync(new KleidungsstueckEntfernt(kleidungsId, benutzername));
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
            return false;
        }
        kleidungLog.trace(System.currentTimeMillis()
                + ": loescheKleidungsstueckEinesBenutzers-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueckEinesBenutzers-Methode - beendet");
        return true;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        kleidungLog
                .debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - gestartet");
        List<Kleidungsstueck> zuLoeschen = this.gebeAlleKleidungsstueckeVomBenutzer(new KleidungsstueckFilter(),
                benutzername);
        List<Long> kleidungsIds = new ArrayList<Long>(); // dient dem zwischenspeichern der Ids fuer den Fehlerfall
        try {
            for (int index = 0; index < zuLoeschen.size(); index++) {
                Kleidungsstueck kleidung = zuLoeschen.get(index);
                this.entityManager.remove(kleidung);
                kleidungsIds.add(kleidung.getKleidungsId());
            }
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - beendet ohne das alle Kleidungsstuecke geloescht wurde");
            // Wenn etwas beim Loeschen aller Kleidungsstueck schief geht muessen alle
            // Kleidungsstuecke bei den Outfits
            // entfernt werden, die geloescht werden konnten.
            if (kleidungsIds.size() > 0) {
                for (int index = 0; index < zuLoeschen.size(); index++) {
                    this.kleidungsstueckEntferntEvent
                            .fireAsync(new KleidungsstueckEntfernt(kleidungsIds.get(index), benutzername));
                }
            }
            return false;
        }
        kleidungLog.trace(System.currentTimeMillis()
                + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstueckeEinesBenutzers-Methode - beendet");
        this.alleKleidungsstueckEntferntEvent.fireAsync(new AlleKleidungsstueckeEntfernt(benutzername));
        return true;
    }

    @Override
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - gestartet");
        try {
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername and k.kleidungsId = :kleidungsId";
            TypedQuery<Kleidungsstueck> query = this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername", benutzername);
            query.setParameter("kleidungsId", kleidungsId);
            Kleidungsstueck erg = query.getSingleResult();
            kleidungLog.trace(System.currentTimeMillis()
                    + ": gebeKleidungsstueckVomBenutzerMitId-Methode - holt ein Kleidungsstueck nach Id fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckVomBenutzerMitId-Methode - beendet");
            return erg;
        } catch (IllegalStateException | PersistenceException e) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": gebeKleidungsstueckVomBenutzerMitId-Methode - holt ein Kleidungsstueck nach Id fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": gebeKleidungsstueckVomBenutzerMitId-Methode - beendet ohne das ein Kleidungsstueck gefunden wurde");
            return null;
        }

    }

    @Override
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(KleidungsstueckFilter filter,
            String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - gestartet");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - gestartet");
        try {
            String querryString = "select k from Kleidungsstueck k where k.benutzername = :benutzername";

            if (filter.farbe != null && !filter.farbe.equals("")) {
                querryString = querryString + " and k.farbe = :farbe";
            }
            if (filter.name != null && !filter.name.equals("")) {
                querryString = querryString + " and k.name = :name";
            }
            if (filter.typ != null && !filter.typ.equals("")) {
                querryString = querryString + " and k.typ = :typ";
            }
            /*
             * if (filter.kategorie != null && !filter.kategorie.equals("")) {
             * querryString = querryString + " and k.kategorie = :kategorie";
             * }
             */

            TypedQuery<Kleidungsstueck> query = this.entityManager.createQuery(querryString, Kleidungsstueck.class);
            query.setParameter("benutzername", benutzername);

            if (filter.farbe != null && !filter.farbe.equals("")) {
                query.setParameter("farbe", filter.farbe);
            }
            if (filter.name != null && !filter.name.equals("")) {
                query.setParameter("name", filter.name);
            }
            if (filter.typ != null && !filter.typ.equals("")) {
                query.setParameter("typ", filter.typ);
            }
            List<Kleidungsstueck> kleidungsList;
            if (filter.kategorie != null && !filter.kategorie.equals("")) {
                kleidungsList = query.getResultList().stream().filter(kleidung -> {
                    return kleidung.getBenutzername().equals(benutzername)
                            && kleidung.besitztKleidungsstueckKategorie(filter.kategorie);
                }).toList();
            } else {
                kleidungsList = query.getResultList();
            }
            kleidungLog.trace(System.currentTimeMillis()
                    + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - holt alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis() + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - beendet");
            return kleidungsList;
        } catch (IllegalStateException | TransactionRequiredException | QueryTimeoutException e) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - holt alle Kleidungsstuecke fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": gebeAlleKleidungsstueckeVomBenutzer-Methode - beendet ohne das Kleidungsstuecke gefunden wurden");
            return new ArrayList<Kleidungsstueck>();
        }

    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - gestartet");
        Kleidungsstueck kleidung = new Kleidungsstueck(dto.groesse, dto.farbe, dto.typ, dto.name, dto.kategorien,
                benutzername);
        this.entityManager.persist(kleidung);
        kleidungLog.trace(System.currentTimeMillis()
                + ": erstelleKleidungsstueckFuerBenutzer-Methode - erstellt ein neues Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleKleidungsstueckFuerBenutzer-Methode - beendet");
        return kleidung.getKleidungsId();
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto,
            String benutzername) {
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - gestartet");
        Kleidungsstueck kleidung = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if (kleidung == null) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - beendet ohne das ein Kleidungsstueck bearbeitet wurde");
            return false;
        }
        if (dto.groesse != null) {
            kleidung.setGroesse(dto.groesse);
        }
        if (dto.farbe != null) {
            kleidung.setFarbe(dto.farbe);
        }
        if (dto.typ != null) {
            kleidung.setTyp(dto.typ);
        }
        kleidung.setBenutzername(benutzername);
        kleidung.setKategorien(dto.kategorien);
        this.entityManager.persist(kleidung);
        kleidungLog.trace(System.currentTimeMillis()
                + ": bearbeiteKleidungsstueckEinesBenutzers-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueckEinesBenutzers - beendet");
        return true;
    }

    @Override
    public Kleidungsstueck gebeKleidungsstueckMitId(long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckMitId-Methode - gestartet");
        Kleidungsstueck outfit = this.entityManager.find(Kleidungsstueck.class, kleidungsId);
        kleidungLog.trace(System.currentTimeMillis()
                + ": gebeKleidungsstueckMitId-Methode - holt ein Kleidungsstueck anhand seiner Id durch Repository");
        kleidungLog.debug(System.currentTimeMillis() + ": gebeKleidungsstueckMitId-Methode - beendet");
        return outfit;
    }

    @Override
    public boolean fuegeKleidungsstueckVomBenutzerKategorieHinzu(long kleidungsId, KategorieDTO kategorie,
            String benutzername) {
        kleidungLog.debug(
                System.currentTimeMillis() + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - gestartet");
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if (kleidungsstueck == null) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - fuegt eine Kategorie dem Kleidungsstueck hinzu durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet ohne das eine Kategorie hinzugefuegt wurde");
            return false;
        }
        try {
            kleidungsstueck.getKategorien().add(kategorie.kategorieNamen);
            this.entityManager.persist(kleidungsstueck);
            kleidungLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - fuegt eine Kategorie dem Kleidungsstueck hinzu durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet");
            return true;
        } catch (PersistenceException ex) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - fuegt eine Kategorie dem Kleidungsstueck hinzu durch Repository");
            kleidungLog.debug(
                    System.currentTimeMillis() + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet");
            return false;
        }
    }

    @Override
    public boolean entferneKategorieVonKleidungsstueckVomBenutzer(long kleidungsId, String kategorie,
            String benutzername) {
        kleidungLog.debug(
                System.currentTimeMillis() + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - gestartet");
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if (kleidungsstueck == null) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - entfernt eine Kategorie aus dem Kleidungsstueck durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet ohne das eine Kategorie entfernt wurde");
            return false;
        }
        try {
            kleidungsstueck.getKategorien().remove(kategorie);
            this.entityManager.persist(kleidungsstueck);
            kleidungLog.trace(System.currentTimeMillis()
                        + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - entfernt eine Kategorie aus dem Kleidungsstueck durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet");
            return true;
        } catch (Exception ex) {
            kleidungLog.trace(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - entfernt eine Kategorie aus dem Kleidungsstueck durch Repository");
            kleidungLog.debug(System.currentTimeMillis()
                    + ": fuegeKleidungsstueckVomBenutzerKategorieHinzu-Methode - beendet ohne das eine Kategorie entfernt wurde");
            return false;
        }

    }

}
