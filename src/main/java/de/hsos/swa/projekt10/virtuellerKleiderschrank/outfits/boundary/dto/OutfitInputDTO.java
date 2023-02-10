package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class OutfitInputDTO {

    @NotBlank(message="Name darf nicht leer sein.")
    public String name;
    public List<String> kategorien;

    public OutfitInputDTO(){
    }

    public OutfitInputDTO(String name, List<String> kategorien) {
        this.name = name;
        this.kategorien = kategorien;
    }
}
