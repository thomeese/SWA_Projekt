package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.ArrayList;

import javax.enterprise.context.Dependent;

@Dependent
public class DTOKonverter {
    public OutfitInputDTO konvert(OutfitFormDTO outfitFormDTO){
        return new OutfitInputDTO(outfitFormDTO.name, new ArrayList<>(), new ArrayList<>() ,false);
    }
}
