package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.validation.constraints.NotBlank;

public class KategorieDTO {
    @NotBlank(message="Groesse darf nicht leer sein")
    public String kategorieNamen;
}
