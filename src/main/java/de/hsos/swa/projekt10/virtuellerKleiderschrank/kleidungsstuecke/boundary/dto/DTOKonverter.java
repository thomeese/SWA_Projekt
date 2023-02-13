package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.ArrayList;

import javax.enterprise.context.Dependent;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;

@Dependent
public class DTOKonverter {
    public  KleidungsstueckInputDTO konvert(KleidungsstueckFormDTO kleidungsstueckFormDTO){
        return new KleidungsstueckInputDTO(kleidungsstueckFormDTO.groesse, kleidungsstueckFormDTO.farbe, kleidungsstueckFormDTO.typ, kleidungsstueckFormDTO.name, new ArrayList<>());
    }    

    public KleidungsstueckOutputDTO konvert(Kleidungsstueck kleidungsstueck)
    {   
        KleidungsstueckOutputDTO kleidungsstueckDTO = new KleidungsstueckOutputDTO(kleidungsstueck.getKleidungsId(), kleidungsstueck.getGroesse(), kleidungsstueck.getFarbe(), kleidungsstueck.getTyp(), kleidungsstueck.getName(), kleidungsstueck.getKategorien());
        return kleidungsstueckDTO;
    }

    public KleidungsstueckExternInputDTO konvert(KleidungsstueckExternFormDTO kleidungsstueckExternFormDTO) {
        return new KleidungsstueckExternInputDTO(kleidungsstueckExternFormDTO.haendlerName, kleidungsstueckExternFormDTO.artikelnummer, kleidungsstueckExternFormDTO.groesse);
    }
}
