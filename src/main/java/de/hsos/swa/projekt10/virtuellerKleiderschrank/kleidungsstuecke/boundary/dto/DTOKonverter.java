package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.ArrayList;

import javax.enterprise.context.Dependent;

@Dependent
public class DTOKonverter {
    
    public KleidungsstueckInputDTO konvert(KleidungsstueckFormDTO kleidungsstueckFormDTO){
        return new KleidungsstueckInputDTO(kleidungsstueckFormDTO.groesse, kleidungsstueckFormDTO.farbe, kleidungsstueckFormDTO.typ, kleidungsstueckFormDTO.name, new ArrayList<>());
    }    
}
