package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckExternInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;

public interface KleidungstueckeProvider {
    public List<Kleidungsstueck> holeAlleKleidungsstuecke(KleidungsstueckFilter filter, String benutzername);

    public Kleidungsstueck holeKleidungsstueckById(long kleidungsId, String benutzername);

    public long erstelleKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO, String benutzername);

    public long erstelleKleidungsstueckMitExterneApi(KleidungsstueckExternInputDTO kleidungsExternDTO, String benutzername) throws ExterneAPIException;

    public boolean loescheKleidungsstueck(long kleidungsId, String benutzer) ;

    public boolean loescheAlleKleidungsstuecke(String benutzer);

    public boolean bearbeiteKleidungsstueck(long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO, String benutzername);

    public boolean fuegeKategorieHinzu(long kleidungsId, KategorieDTO kategorie, String benutzername);

    public boolean entferneKategorie(long kleidungsid, String kategorie, String benutzername);
}
