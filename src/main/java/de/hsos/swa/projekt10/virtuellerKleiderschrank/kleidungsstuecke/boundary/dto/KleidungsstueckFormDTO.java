package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.ws.rs.FormParam;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckFormDTO {

    @FormParam("size")
    public String groesse;
    @FormParam("color")
    public Farbe farbe;
    @FormParam("type")
    public Typ typ;
    @FormParam("name")
    public String name;
}
