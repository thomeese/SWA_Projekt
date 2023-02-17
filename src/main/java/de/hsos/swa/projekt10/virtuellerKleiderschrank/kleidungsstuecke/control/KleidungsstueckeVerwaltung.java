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
/**
 * Realisiert die Funktionen fuer das Verwalten jeglicher Kleidungsstuecke.
 * @author Manuel Arling
 */
public class KleidungsstueckeVerwaltung implements KleidungstueckeProvider {
    
    @Inject
    private KleidungsstueckKatalog kKatalog;

    @Inject
    private KleidungsstueckVonOnlineHaendler kOnlineHaendler;

    @Override
    public List<Kleidungsstueck> holeAlleKleidungsstuecke(KleidungsstueckFilter filter, String benutzername) {
        return this.kKatalog.gebeAlleKleidungsstueckeVomBenutzer(filter,benutzername);
    }

    @Override
    public Kleidungsstueck holeKleidungsstueckById(long kleidungsId, String benutzername) {
        return this.kKatalog.gebeKleidungsstueckVomBenutzerMitId(kleidungsId, benutzername);
    }

    @Override
    public long erstelleKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO, String benutzername) {
        return this.kKatalog.erstelleKleidungsstueckFuerBenutzer(kleidungsDTO, benutzername);
    }

    @Override
    public long erstelleKleidungsstueckMitExterneApi(KleidungsstueckExternInputDTO kleidungsExternDTO, String benutzername) throws ExterneAPIException{
        KleidungsstueckInputDTO kleidungsstueckInputDTO;
        switch(kleidungsExternDTO.haendlerName) {
            case "hm":
                kleidungsstueckInputDTO = this.kOnlineHaendler.holeKleidungsstueckVonHM(kleidungsExternDTO.artikelnummer, kleidungsExternDTO.groesse);
            break;
            default:
                throw new ExterneAPIException("Der angegebenen Haendler wird nicht unterstuetzt. Folgende Haendler werden Unterstuetzt: hm", ExterneAPIException.NOTSUPPORTED);
        }
        return this.kKatalog.erstelleKleidungsstueckFuerBenutzer(kleidungsstueckInputDTO, benutzername);
    }

    @Override
    public boolean loescheKleidungsstueck(long kleidungsId, String benutzer) {
        return this.kKatalog.loescheKleidungsstueckEinesBenutzers(kleidungsId, benutzer);
    }

    @Override
    public boolean loescheAlleKleidungsstuecke(String benutzer) {
        return this.kKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzer);
    }

    @Override
    public boolean bearbeiteKleidungsstueck(long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO, String benutzername) {
        return this.kKatalog.bearbeiteKleidungsstueckEinesBenutzers(kleidungsId, kleidungsstueckInputDTO, benutzername);
    }

    @Override
    public boolean fuegeKategorieHinzu(long kleidungsId, KategorieDTO kategorie, String benutzername) {
        return this.kKatalog.fuegeKleidungsstueckVomBenutzerKategorieHinzu(kleidungsId, kategorie, benutzername);
    }

    @Override
    public boolean entferneKategorie(long kleidungsid, String kategorie, String benutzername) {
        return this.kKatalog.entferneKategorieVonKleidungsstueckVomBenutzer(kleidungsid, kategorie, benutzername);
    }
}
