package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.KleidungsstueckVonOnlineHaendler;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckExternInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;

@ApplicationScoped
public class KleidungsstueckeVerwaltung {
    
    @Inject
    private KleidungsstueckKatalog kKatalog;

    @Inject
    private KleidungsstueckVonOnlineHaendler kOnlineHaendler;

    public List<Kleidungsstueck> holeAlleKleidungsstuecke(KleidungsstueckFilter filter, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzer(filter,benutzername);
    }

    public Kleidungsstueck holeKleidungsstueckById(long kleidungsId, String benutzername) {
        return this.kKatalog.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
    }

    public long erstelleKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO, String benutzername) {
        return this.kKatalog.erstelleKleidungsstueckFuerBenutzer(kleidungsDTO, benutzername);
    }

    public long erstelleKleidungsstueckMitExterneApi(KleidungsstueckExternInputDTO kleidungsExternDTO, String benutzername) throws ExterneAPIException{
        KleidungsstueckInputDTO kleidungsstueckInputDTO;
        switch(kleidungsExternDTO.haendlerName) {
            case "hm":
                kleidungsstueckInputDTO = this.kOnlineHaendler.holeKleidungsstueckVonHM(kleidungsExternDTO.artikelnummer, kleidungsExternDTO.groesse);
            break;
            default:
            return -1;
        }
        return this.kKatalog.erstelleKleidungsstueckFuerBenutzer(kleidungsstueckInputDTO, benutzername);
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

    public boolean fuegeKategorieHinzu(long kleidungsId, KategorieDTO kategorie, String benutzername) {
        return this.kKatalog.fuegeKleidungsstueckVomBenutzerKategorieHinzu(kleidungsId, kategorie, benutzername);
    }

    public boolean entferneKategorie(long kleidungsid, String kategorie, String benutzername) {
        return this.kKatalog.entferneKategorieVonKleidungsstueckVomBenutzer(kleidungsid, kategorie, benutzername);
    }
}
