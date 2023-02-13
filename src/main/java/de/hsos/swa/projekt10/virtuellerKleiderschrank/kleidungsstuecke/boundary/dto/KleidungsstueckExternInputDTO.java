package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
public class KleidungsstueckExternInputDTO {

    @NotBlank(message="HaendlerName darf nicht leer sein")
    public String haendlerName;

    @NotBlank(message="Artikelnummer darf nicht leer sein")
    public String artikelnummer;

    public String groesse;

    public KleidungsstueckExternInputDTO(){
    }
}