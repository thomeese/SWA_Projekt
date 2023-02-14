package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

import javax.validation.Valid;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;


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
    public long erstelleKleidungsstueckFuerBenutzer(@Valid KleidungsstueckInputDTO dto, String benutzername);
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
     * Holt ein Kleiddungstueck Anhand seiner Id aus der Datenbank.
     * Wenn kein Kleidungsstueck gefunden wird, wird null zurueckgegeben.
     * 
     * @param kleidungsId Id des Kleidungsstuecks
     * @return Kleidungssteuck gefundenes Kleidugnssteuck
     * @author Thomas Meese
     */
    public Kleidungsstueck gebeKleidungsstueckMitId(long kleidungsId);
    /**
     * Holt die Kleidungsstuecke fuer den Benutzer aus der Datenank.
     * Das Ergebnis der Anfrage ist gefiltert. Es kann nach Name, Farbe, Typ und Kategorie
     * zusaetzlich gefiltert werden. Wenn keine Filterparameter vorhanden sind, werden alle
     * Kleidungsstuecke des Benutzers zurueckgegeben. Wenn keine Kleidungsstuecke gefunden
     * werden, wird eine Leere Liste zurueck gegeben.
     * @param benutzername Benutzer dessen Kleidungsstuecke geholt werden sollen
     * @param filter moegliche Filterparameter
     * @return List&ltKleidungsstueck&gt Liste mit den gefundenen Kleidungsstuecken
     * @author Thomas Meese
     */
    public List<Kleidungsstueck> gebeAlleKleidungsstueckeVomBenutzer(KleidungsstueckFilter filter, String benutzername);

    /**
     * Fuegt einem Kleidungsstueck eines Benutzers eine Kategorie hinzu.
     * Aenderung wird anschliessend in der Datenbank gespeichert.
     * 
     * @param kelidungsId Id des Kleidungsstuecks
     * @param kategorie Kategorie die ergaenzt werden soll
     * @param benutzername Benutzer zu dem das Kleidungsstueck gehoert
     * @return boolean true bei erfolg, false bei misserfolg
     * @author Thomas Meese
     */
    public boolean fuegeKleidungsstueckVomBenutzerKategorieHinzu(long kelidungsId, KategorieDTO kategorie, String benutzername);

    /**
     * Entfernt eine Kategorie aus einem Kleidungsstueck eines Benutzers.
     * Aenderung wird anschliessend in der Datenbank gespeichert.
     * 
     * @param kelidungsId Id des Kleidungsstuecks
     * @param kategorie Kategorie die ergaenzt werden soll
     * @param benutzername Benutzer zu dem das Kleidungsstueck gehoert
     * @return boolean true bei erfolg, false bei misserfolg
     * @author Thomas Meese
     */
    public boolean entferneKategorieVonKleidungsstueckVomBenutzer(long kleidungsId, String kategorie, String benutzername);

}  