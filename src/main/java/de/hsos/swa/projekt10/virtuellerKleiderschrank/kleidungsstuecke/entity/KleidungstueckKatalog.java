package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

public interface KleidungstueckKatalog {
    public long erstelleKleidungsstueckFuerBenutzer(KleidungstueckInputDTO dto, String benutzername);
    public boolean bearbeiteKleidungstueckEinesBenutzers(long kleidungsId, KleidungstueckInputDTO dto, String benutzername);
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername);
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername);
    public Kleidungstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername);
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzer(String benutzername);
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinesTyp(Typ typ,String benutzername);
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerMitNamen(String name, String benutzername);
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinerFarbe(Farbe farbe, String benutzername);
    public List<Kleidungstueck> gebeAlleKleingsstueckVomBenutzerEinerKategorie(String kategorie, String benutzername);
}
