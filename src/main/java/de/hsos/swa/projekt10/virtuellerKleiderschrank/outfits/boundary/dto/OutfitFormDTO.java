package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import javax.ws.rs.FormParam;

public class OutfitFormDTO {
    @FormParam("name")
    public String name;
}
