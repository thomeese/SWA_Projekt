package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;

public class KleidungsstueckInputDTO {

    @NotBlank(message="Groesse darf nicht leer sein")
    public String groesse;
    @NotNull(message="Farbe darf nicht leer sein")
    public Farbe farbe;
    @NotNull(message="Typ darf nicht leer sein")
    public Typ typ;
    @NotBlank(message="HaendlerName darf nicht leer sein")
    public String name;
    public List<String> kategorien;
    
    public KleidungsstueckInputDTO() {
    }

    public KleidungsstueckInputDTO(String groesse, Farbe farbe, Typ typ, String name, List<String> kategorien) {
        this.groesse = groesse;
        this.farbe = farbe;
        this.typ = typ;
        this.name = name;
        this.kategorien = kategorien;
    }
}
