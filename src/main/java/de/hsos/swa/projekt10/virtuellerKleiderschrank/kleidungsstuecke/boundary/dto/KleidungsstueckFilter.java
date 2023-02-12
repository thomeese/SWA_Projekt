package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.ws.rs.QueryParam;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckFilter {
    @QueryParam("name")
    public String name;
    @QueryParam("type")
    public Typ typ;
    @QueryParam("color")
    public Farbe farbe;
    @QueryParam("category")
    public String kategorie;
}
