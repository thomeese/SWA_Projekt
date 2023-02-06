package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckRepository implements KleidungsstueckKatalog{

    @Inject
    private EntityManager entityManager;

    @Override
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername) {
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidungsstueck == null){
            return false;
        }
        this.entityManager.remove(kleidungsstueck);
        return true;
        
    }

    @Override
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        List<Kleidungsstueck> zuLoeschen = this.gebeAlleKleingsstueckVomBenutzer(benutzername);
        for(int index = 0; index < zuLoeschen.size(); index++){
            this.entityManager.remove(zuLoeschen.get(index)); // oder darf man da die ganze Liste uebergeben?
        }
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getKleidungsId() == kleidungsId;
        }).findFirst().get();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinesTyp(Typ typ, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getTyp() == typ;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerMitNamen(String name, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getName().equals(name);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinerFarbe(Farbe farbe, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getFarbe() == farbe;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.besitztKleidungsstueckKategorie(kategorie);
        }).toList();
    }

    @Override
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean bearbeiteKleidungstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto,
            String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzer(String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername);
        }).toList();
    }

    
}
