package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary;

import javax.validation.constraints.NotBlank;

public class NutzerDTO {
    @NotBlank
    public String vorname;
    @NotBlank
    public String nachname;
    @NotBlank
    public String email;
    @NotBlank
    public String benutzername;
    @NotBlank
    public String passwort;

    public NutzerDTO() {

    }

    public NutzerDTO(String vorname, String nachname, String email, String benutzername, String passwort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }
}
