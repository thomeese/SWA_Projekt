package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;


public interface KleidungsstueckKatalog {
    /**
     * Erstellt eine neues Kleidungsstuek fuer einen Benutzer.
     * Das Kleidungsstueck wird anschliessend in der Datenbank gespeichert.
     * 
     * @param dto Daten die fuer das Erstellen benoetigt werden
     * @param benutzername Benutzer fuer den das Kleidungsstueck erstellt werden soll
     * @return long Id des erstellten Kleidungsstuecks
     * @author Thomas Meese
     */
    public long erstelleKleidungsstueckFuerBenutzer(KleidungsstueckInputDTO dto, String benutzername);
    /**
     * Veraendert die den Inhalt der Attribute eines Kleidungsstuecks.
     * Aenderungen werden anschliessend in der Datenbank gespeichert.
     * 
     * @param kleidungsId Id des zu bearbeitenden Kleidungsstueck
     * @param dto Daten mit den neuen Werten
     * @param benutzername Benutzer dessen Kleidungsstueck geaendert werden soll
     * @return boolean true wenn etwas geandert wurde, false wenn nicht
     * @author Thomas Meese
     */
    public boolean bearbeiteKleidungsstueckEinesBenutzers(long kleidungsId, KleidungsstueckInputDTO dto, String benutzername);
    /**
     * Entfernt ein Kleidungsstueck vom Benutzer aus der Datenbank.
     * 
     * @param kleidungsId zu entfernendes Kleidungsstueck
     * @param benutzername Benutzer dessen Kleidungsstueck entfernt werden soll
     * @return boolean true bei erfolg, false bei Fehlschlag
     * @author Thomas Meese
     */
    public boolean loescheKleidungsstueckEinesBenutzers(long kleidungsId, String benutzername);
    /**
     * Entfernt alle Kleidungsstuecke vom Benutzer aus der Datenbank.
     * 
     * @param benutzername Benutzer dessen Kleidungsstuecke entfernt werden sollen
     * @return boolean true bei erfolg, false bei Fehlschlag
     * @author Thomas Meese
     */
    public boolean loescheAlleKleidungsstueckeEinesBenutzers(String benutzername);
    /**
     * Holt ein Kleidungsstueck anhand seiner Id fuer den Benutzer aus der Datenbank.
     * Wenn kein Kleidungsstueck gefunden wird, wird Null returned.
     * 
     * @param kleidungsId Id des Kleidungsstuecks
     * @param benutzername Benutzer dessen Kleidungsstueck geholt werden soll
     * @return Kleidungsstueck gefundenes Kleidunsstueck
     * @author Thomas Meese
     */
    public Kleidungsstueck gebeKleidungsstueckVomBenutzerMitId(long kleidungsId, String benutzername);
    /**
     * Holt alle Kleidungsstuecke fuer den Benutzer aus der Datenank.
     * Wenn keine Kleidungsstuecke gefunden werden, wird eine Leere Liste zurueck gegeben.
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(String benutzername);
    /**
     * Holt alle Kleidungsstuecke eines Typs fuer den Benutzer aus der Datenank.
     * Wenn keine Kleidungsstuecke gefunden werden, wird eine Leere Liste zurueck gegeben.
     * 
     * @param typ Typ der Kleidungsstuecke
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinesTyp(Typ typ,String benutzername);
    /**
     * Holt alle Kleidungsstuecke Anhand des Names (bzw.  der Marke oder  der Beschreibung)
     * fuer den Benutzer aus der Datenank. Wenn keine Kleidungsstuecke gefunden werden, 
     * wird eine Leere Liste zurueck gegeben.
     * 
     * @param name Name/Marke/Beschreibung des Kleidungsstuecks
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerMitNamen(String name, String benutzername);
    /**
     * Holt alle Kleidungsstuecke einer Farbe fuer den Benutzer aus der Datenank.
     * Wenn keine Kleidungsstuecke gefunden werden, wird eine Leere Liste zurueck gegeben.
     * 
     * @param farbe Farbe der Kleidungsstuecke
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerFarbe(Farbe farbe, String benutzername);
    /**
     * Holt alle Kleidungsstuecke einer Kategorie fuer den Benutzer aus der Datenank.
     * Wenn keine Kleidungsstuecke gefunden werden, wird eine Leere Liste zurueck gegeben.
     * 
     * @param kategorie Kategorie der Kleidungsstuecke
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzerEinerKategorie(String kategorie, String benutzername);
}
