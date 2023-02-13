package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.validation.constraints.NotBlank;

public class KleidungsstueckExternInputDTO {

    @NotBlank(message="HaendlerName darf nicht leer sein")
    public String haendlerName;

    @NotBlank(message="Artikelnummer darf nicht leer sein")
    public String artikelnummer;

    public String groesse;

    public KleidungsstueckExternInputDTO(){
    }

    public KleidungsstueckExternInputDTO(String haendlerName, String artikelnummer, String groesse){
        this.haendlerName = haendlerName;
        this.artikelnummer = artikelnummer;
        this.groesse = groesse;
    }
}