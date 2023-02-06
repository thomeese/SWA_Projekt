package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;

public interface KleidungsstueckKatalog {
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername);
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto, String benutzername);
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername);
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername);
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(String benutzername);
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinesTyp(Typ typ,String benutzername);
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerMitNamen(String name, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(Farbe farbe, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(String kategorie, String benutzername);
}
