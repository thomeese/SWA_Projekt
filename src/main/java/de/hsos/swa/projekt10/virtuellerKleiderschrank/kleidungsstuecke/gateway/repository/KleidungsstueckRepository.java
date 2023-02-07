package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

@Dependent
public class KleidungsstueckRepository implements KleidungsstueckKatalog{

    @Inject
    private EntityManager entityManager;

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername) {
        Kleidungsstueck kleidungsstueck = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidungsstueck == null){
            return false;
        }
        this.entityManager.remove(kleidungsstueck);
        return true;    
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername) {
        List<Kleidungsstueck> zuLoeschen = this.gebeAlleKleidungsstueckeVomBenutzer(benutzername);
        for(int index = 0; index < zuLoeschen.size(); index++){
            this.entityManager.remove(zuLoeschen.get(index));
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
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinesTyp(Typ typ, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getTyp() == typ;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerMitNamen(String name, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getName().equals(name);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(Farbe farbe, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.getFarbe() == farbe;
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername) && kleidung.besitztKleidungsstueckKategorie(kategorie);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(String benutzername) {
        TypedQuery<Kleidungsstueck> query= this.entityManager.createQuery("select k from Kleidungsstueck k", Kleidungsstueck.class);
        
        return query.getResultList().stream().filter(kleidung -> {
            return kleidung.getBenutzername().equals(benutzername);
        }).toList();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername) {
        Kleidungsstueck kleidung = new Kleidungsstueck(dto.groesse, dto.farbe, dto.type, benutzername, dto.kategorien, benutzername);
        this.entityManager.persist(kleidung);
        return kleidung.getKleidungsId();
    }

    @Override
    @Transactional(value=TxType.REQUIRES_NEW)
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto,
            String benutzername) {
        Kleidungsstueck kleidung = this.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
        if(kleidung == null){
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

        return true;
    }

    
}
