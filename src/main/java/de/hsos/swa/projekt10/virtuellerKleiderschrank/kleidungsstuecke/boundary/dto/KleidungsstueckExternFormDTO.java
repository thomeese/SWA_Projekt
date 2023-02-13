package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import javax.ws.rs.FormParam;

public class KleidungsstueckExternFormDTO {
    @FormParam("haendlerName")
    public String haendlerName;
    @FormParam("articlenumber")
    public String artikelnummer;
    @FormParam("size")
    public String groesse;
}
