package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;


public class OutfitOutputDTO {
    long outfitId;
    String name;
    List<String> kategorien;
    boolean istGeteilt;

    public OutfitOutputDTO() {
    }

    public OutfitOutputDTO(long outfitId, String name, List<String> kategorien, boolean istGeteilt) {
        this.outfitId = outfitId;
        this.name = name;
        this.kategorien = kategorien;
        this.istGeteilt = istGeteilt;
    }

    public static class Converter {
        //TODO prüfen, ob Inject benutzt werden soll/kann
        public static OutfitOutputDTO toOutfitOutputDTO(Outfit outfit)
        {
            //TODO DTO eventuell hier nochmal Validieren? oder sicher vollständig, da schon in Ressource validiert wird und nur dort InputDTO erstellt wird.
            return new OutfitOutputDTO(outfit.outfitId, outfit.name, outfit.kategorien, outfit.istGeteilt);

        }
    }
}
