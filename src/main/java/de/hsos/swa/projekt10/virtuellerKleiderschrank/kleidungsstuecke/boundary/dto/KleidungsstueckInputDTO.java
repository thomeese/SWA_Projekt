package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.List;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Type;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;

public class KleidungsstueckInputDTO {
    String groesse;
    Farbe farbe;
    Typ type;
    String name;
    List<String> kategorien;
    
    public KleidungsstueckInputDTO() {
    }

    public KleidungsstueckInputDTO(String groesse, Farbe farbe, Typ type, String name, List<String> kategorien) {
        this.groesse = groesse;
        this.farbe = farbe;
        this.type = type;
        this.name = name;
        this.kategorien = kategorien;
    }
}