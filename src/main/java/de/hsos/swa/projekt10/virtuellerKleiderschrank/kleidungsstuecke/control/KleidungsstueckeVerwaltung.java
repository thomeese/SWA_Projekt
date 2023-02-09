package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

@ApplicationScoped
public class KleidungsstueckeVerwaltung {
    
    @Inject
    private KleidungsstueckKatalog kKatalog;

    public List<Kleidungsstueck> holeAlleKleidungsstuecke(String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzer(benutzername);
    }

    public Kleidungsstueck holeKleidungsstueckById(long kleidungsId, String benutzername) {
        return this.kKatalog.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
    }

    public long erstelleKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO, String benutzername) {
        return this.kKatalog.erstelleKleidungsstueckFuerBenutzer(kleidungsDTO, benutzername);
    }

    public boolean loescheKleidungsstueck(long kleidungsId, String benutzer) {
        return this.kKatalog.loescheKleidungsstueckEinesBenutzers(kleidungsId, benutzer);
    }

    public boolean loescheAlleKleidungsstuecke(String benutzer) {
        return this.kKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzer);
    }

    public boolean bearbeiteKleidungsstueck(long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO, String benutzername) {
        return this.kKatalog.bearbeiteKleidungsstueckEinesBenutzers(kleidungsId, kleidungsstueckInputDTO, benutzername);
    }

    public List<Kleidungsstueck> filterNachFarbe(Farbe farbe, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(farbe, benutzername);
    }

    public List<Kleidungsstueck> filterNachKategorie(String kategorie, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(kategorie, benutzername);
    }

    public List<Kleidungsstueck> filterNachTyp(Typ typ, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzerEinesTyp(typ, benutzername);
    }

    public List<Kleidungsstueck> filterNachName(String name, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzerMitNamen(name, benutzername);
    }
}
