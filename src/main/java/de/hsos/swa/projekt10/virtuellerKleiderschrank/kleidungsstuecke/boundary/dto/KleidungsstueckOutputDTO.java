package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.List;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;

public class KleidungsstueckOutputDTO {
    long kleidungsId;
    String groesse;
    Farbe farbe;
    Typ typ;
    String name;
    List<String> kategorien;

    public KleidungsstueckOutputDTO() {
    }


    public KleidungsstueckOutputDTO(long kleidungsId, String groesse, Farbe farbe, Typ typ, String name, List<String> kategorien) {
        this.kleidungsId = kleidungsId;
        this.groesse = groesse;
        this.farbe = farbe;
        this.typ = typ;
        this.name = name;
        this.kategorien = kategorien;
    }

    public static class Converter {
        //TODO prüfen, ob Inject benutzt werden soll/kann
        public static KleidungsstueckOutputDTO toKleidungsstueckOutputDTO (Kleidungsstueck kleidungsstueck)
        {
            //TODO DTO eventuell hier nochmal Validieren? oder sicher vollständig, da schon in Ressource validiert wird und nur dort InputDTO erstellt wird.
            return new KleidungsstueckOutputDTO(kleidungsstueck.getKleidungsId(), kleidungsstueck.getGroesse(), kleidungsstueck.getFarbe(), kleidungsstueck.getTyp(), kleidungsstueck.getName(), kleidungsstueck.getKategorien());
        }
    }  
}
