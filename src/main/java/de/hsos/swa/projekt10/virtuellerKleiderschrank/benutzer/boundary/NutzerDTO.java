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
}
