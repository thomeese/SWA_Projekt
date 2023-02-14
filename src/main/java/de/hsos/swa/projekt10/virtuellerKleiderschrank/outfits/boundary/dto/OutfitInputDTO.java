package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OutfitInputDTO {

    @NotBlank(message="Name darf nicht leer sein.")
    public String name;
    @NotNull(message="Es muss eine liste von Kategorien geben.")
    public List<String> kategorien;

    public OutfitInputDTO(){
    }

    public OutfitInputDTO(String name, List<String> kategorien) {
        this.name = name;
        this.kategorien = kategorien;
    }
}
