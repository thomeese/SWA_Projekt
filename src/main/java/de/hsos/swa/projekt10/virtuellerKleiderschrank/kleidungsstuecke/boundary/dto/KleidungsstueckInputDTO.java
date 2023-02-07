package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;

public class KleidungsstueckInputDTO {
    public String groesse;
    public Farbe farbe;
    public Typ type;
    public String name;
    public List<String> kategorien;
    
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
