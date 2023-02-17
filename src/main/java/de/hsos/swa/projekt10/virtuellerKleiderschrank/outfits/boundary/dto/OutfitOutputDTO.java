package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import shared.LinkDeserializer;
import shared.LinkSerializer;

public class OutfitOutputDTO {
    public long outfitId;
    public String name;
    public List<String> kategorien;
    public List<Long> kleidungsstuecke;
    public boolean istGeteilt;

    @JsonSerialize(using = LinkSerializer.class)
    @JsonDeserialize(using = LinkDeserializer.class)
    public List<Link> links = new ArrayList<>();

    public OutfitOutputDTO() {
    }

    public OutfitOutputDTO(long outfitId, String name, List<String> kategorien, List<Long> kleidungsstuecke,
            boolean istGeteilt) {
        this.outfitId = outfitId;
        this.name = name;
        this.kategorien = kategorien;
        this.kleidungsstuecke = kleidungsstuecke;
        this.istGeteilt = istGeteilt;
    }

    public static class Converter {
        // TODO prüfen, ob Inject benutzt werden soll/kann
        public static OutfitOutputDTO toOutfitOutputDTO(Outfit outfit) {
            // TODO DTO eventuell hier nochmal Validieren? oder sicher vollständig, da schon
            // in Ressource validiert wird und nur dort InputDTO erstellt wird.
            return new OutfitOutputDTO(outfit.getOutfitId(), outfit.getName(), outfit.getKategorien(),
                    new ArrayList<>(outfit.getKleidungsstuecke()), outfit.isGeteilt());

        }
    }


    public void addLink(Link link) {
        this.links.add(link);
    }

    @Override
    /**
     * @author Thomas Meese
     */
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        OutfitOutputDTO other = (OutfitOutputDTO) obj;

        if (other.outfitId != this.outfitId) {
            return false;
        }

        if (!this.name.equals(other.name)) {
            return false;
        }

        if (other.istGeteilt != this.istGeteilt) {
            return false;
        }
        if (this.kategorien.size() != other.kategorien.size()) {
            return false;
        }
        for (int index = 0; index < this.kategorien.size(); index++) {
            if (!this.kategorien.get(index).equals(other.kategorien.get(index))) {
                return false;
            }
        }

        if (this.kleidungsstuecke.size() != other.kleidungsstuecke.size()) {
            return false;
        }

        for (int index = 0; index < this.kleidungsstuecke.size(); index++) {
            if (this.kleidungsstuecke.get(index)!= other.kleidungsstuecke.get(index)) {
                return false;
            }
        }
        return true;
    }

}
