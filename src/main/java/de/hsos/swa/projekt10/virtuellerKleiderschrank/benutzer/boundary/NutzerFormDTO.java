package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary;

import javax.ws.rs.FormParam;

public class NutzerFormDTO {
    @FormParam("vorname")
    public String vorname;
    @FormParam("nachname")
    public String nachname;
    @FormParam("email")
    public String email;
    @FormParam("benutzername")
    public String benutzername;
    @FormParam("passwort")
    public String passwort;
}
