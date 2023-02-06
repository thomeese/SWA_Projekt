package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

public interface KleidungsstueckKatalog {
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername);
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto, String benutzername);
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername);
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername);
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzer(String benutzername);
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinesTyp(Typ typ,String benutzername);
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerMitNamen(String name, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinerFarbe(Farbe farbe, String benutzername);
    public List<Kleidungsstueck> gebeAlleKleingsstueckVomBenutzerEinerKategorie(String kategorie, String benutzername);
}
