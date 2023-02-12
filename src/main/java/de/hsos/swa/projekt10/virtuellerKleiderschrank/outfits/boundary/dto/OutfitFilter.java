package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import javax.ws.rs.QueryParam;

public class OutfitFilter {
    @QueryParam("name")
    public String name;
    @QueryParam("category")
    public String kategorie;
}
