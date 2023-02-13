package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;


public class OutfitOutputDTO {
    public long outfitId;
    public String name;
    public List<String> kategorien;
    public List<Long> kleidungsstuecke;
    public boolean istGeteilt;

    public OutfitOutputDTO() {
    }

    public OutfitOutputDTO(long outfitId, String name, List<String> kategorien,  List<Long> kleidungsstuecke, boolean istGeteilt) {
        this.outfitId = outfitId;
        this.name = name;
        this.kategorien = kategorien;
        this.kleidungsstuecke = kleidungsstuecke;
        this.istGeteilt = istGeteilt;
    }

    public static class Converter {
        //TODO prüfen, ob Inject benutzt werden soll/kann
        public static OutfitOutputDTO toOutfitOutputDTO(Outfit outfit)
        {
            //TODO DTO eventuell hier nochmal Validieren? oder sicher vollständig, da schon in Ressource validiert wird und nur dort InputDTO erstellt wird.
            return new OutfitOutputDTO(outfit.getOutfitId(), outfit.getName(), outfit.getKategorien(), outfit.getKleidungsstuecke(),outfit.isGeteilt());

        }
    }
}
