package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.List;

public class OutfitInputDTO {
    String name;
    List<String> kategorien;

    public OutfitInputDTO(){
    }

    public OutfitInputDTO(String name, List<String> kategorien) {
        this.name = name;
        this.kategorien = kategorien;
    }
}
