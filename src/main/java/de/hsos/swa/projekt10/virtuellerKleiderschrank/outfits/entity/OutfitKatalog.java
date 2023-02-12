package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity;

import java.util.List;

import javax.validation.Valid;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;

public interface OutfitKatalog {
    /**
     * Erstellt fuer den Benutzer ein neuees Outfit. Das Outfit wird
     * andschliessend in der Datenbank gespeichert.
     * 
     * @param dto Daten die fuer das Erstellen benoetigt werden.
     * @param benutzername Benutzer fuer den das Outfit erstellt werden soll.
     * @return long Id des erstellten Outfits
     * @author Thomas Meese
     */
    public long erstelleOutfitFuerEinenBenutzer(@Valid OutfitInputDTO dto, String benutzername);

    /**
     * Veraendert den Inhalt eines Outfits des Benutzers. Die Aenderungen
     * werden anschliessen in der Datenbank gespeichert.
     * 
     * @param dto neuer Inhalt des OutfitsS
     * @param outfitId Id des zu bearbeitenden Outfits
     * @param benutzername Benutzer dessen Outfir bearbeitet werden soll
     * @return boolean true wenn eine Bearbeitung erfolgt ist, false wenn nicht
     * @author Thomas Meese
     */
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, long outfitId, String benutzername);

    /**
     * Entfernt ein Outfit von einem Benutzer aus der Datenbank.
     * 
     * @param outfitId zu loeschendes Outfit
     * @param benutzername Benutzer dessen Outfit geloescht wurde
     * @return boolean true wenn es geloescht wurde, false wenn nicht
     * @author Thomas Meese
     */
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername);

    /**
     * Entfernt alle Outfits von einem Benutzer aus der Datenbank.
     * 
     * @param benutzername Benutzer dessen Outfits geloscht werden sollen
     * @return boolean true wenn alle Outfits geloescht wurden, false wenn nicht
     * @author Thomas Meese
     */
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername);

    /**
     * Fuegt ein Kleidungsteuck einem Outfit des Benutzers hinzu. Die Aenderung
     * wird anschliessend in der Datenbank gespeichert.
     * 
     * @param kleidungsId Id des Kleidungsstuecks
     * @param outfitId Id des Outfits
     * @param benutzername Benutzer dessen Outfit bearbeitet werden soll
     * @return Outfit veraendertet Outfit
     * @author Thomas Meese
     */
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername);

    /**
     * 
     * @param kleidungsId Id des Kleidungsstuecks
     * @param outfitId Id des Outfits
     * @param benutzername Benutzer dessen Outfit bearbeitet werden soll
     * @return Outfit veraendertet Outfit
     * @author Thomas Meese
     */
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername);

      /**
     * Holt die Outfits fuer den Benutzer aus der Datenank.
     * Das Ergebnis der Anfrage ist gefiltert. Es kann nach Name und Kategorie
     * zusaetzlich gefiltert werden. Wenn keine Filterparameter vorhanden sind, werden alle
     * Outfits des Benutzers zurueckgegeben. Wenn keine Outfits gefunden
     * werden, wird eine Leere Liste zurueck gegeben.
     * @param benutzername Benutzer dessen Outfits geholt werden sollen
     * @param filter moegliche Filterparameter
     * @return List&ltOutfit&gt Liste mit den gefundenen Outfits
     * @author Thomas Meese
     */
    public List<Outfit> gebeAlleOutfitsVomBenutzer(OutfitFilter filter, String benutzername);

    /**
     * Holt ein Outfit des Benutzers anhand seiner Id aus der Datenbank. Wenn kein 
     * Outfit gefunden wurde wird null zurueckgegeben.
     * 
     * @param outfitId Id des zu holenden Outfits
     * @param benutzername Benutzer dessen Outfit geholt werden soll
     * @return Outfit gefundenes Outfit
     */
    public Outfit gebeOutfitVomBenutzerMitId(long outfitId, String benutzername);

    /**
     * Holt ein geteiltes Outfit aus der Datenbank. Wenn kein Outfit gefunden wurde
     * wird null zurueckgegeben.
     * @param outfitId Id des geteilten Outfits
     * @return Outfit gefundenes Outfit
     */
    public Outfit gebeGeteilitesOutfit(long outfitId);
}
