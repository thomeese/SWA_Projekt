package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;

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
}
