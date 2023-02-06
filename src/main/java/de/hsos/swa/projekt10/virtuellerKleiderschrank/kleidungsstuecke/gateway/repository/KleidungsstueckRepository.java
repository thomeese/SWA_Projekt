package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungstueckKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckRepository implements KleidungstueckKatalog{

    @Inject
    private EntityManager entityManager;

    @Override
    public long erstelleKleidungsstueckFuerBenutzer(KleidungstueckInputDTO dto, String benutzername) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean bearbeiteKleidungstueckEinesBenutzers(long kleidungsId, KleidungstueckInputDTO dto,
            String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername) {
        Kleidungstueck kleidungstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidungstueck == null){
            return false;
        }
        this.entityManager.remove(kleidungstueck);
        return true;
        
    }

    @Override
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        List<Kleidungstueck> zuLoeschen = this.gebeAlleKleingsstueckVomBenutzer(benutzername);
        for(int index = 0; index < zuLoeschen.size(); index++){
            this.entityManager.remove(zuLoeschen.get(index)); // oder darf man da die ganze Liste uebergeben?
        }
        return true;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public Kleidungstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getKleidungsId() == kleidungsId;
        }).findFirst().get();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinesTyp(Typ typ, String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getTyp() == typ;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerMitNamen(String name, String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getName().equals(name);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinerFarbe(Farbe farbe, String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getFarbe() == farbe;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.besitztKleidungsstueckKategorie(kategorie);
        }).toList();
    }

    @Override
    public long erstelleKleidungsstueckFuerBenutzer(KleidungstueckInputDTO dto, String benutzername) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean bearbeiteKleidungstueckEinesBenutzers(long kleidungsId, KleidungstueckInputDTO dto,
            String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzer(String benutzername) {
        TypedQuery<Kleidungstueck> query= this.entityManager.createQuery("select k from Kleidungstueck k", Kleidungstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername);
        }).toList();
    }

    
}
