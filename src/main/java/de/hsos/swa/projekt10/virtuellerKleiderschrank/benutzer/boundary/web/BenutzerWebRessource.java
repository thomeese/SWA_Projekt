package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.web;

import javax.ws.rs.Produces;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.NutzerDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.NutzerFormDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.control.Benutzerverwaltung;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

@Path("/web/register")
public class BenutzerWebRessource {
    @Inject 
    private Benutzerverwaltung benutzerverwaltung;

    @CheckedTemplate
    public static class Template {
        public static native TemplateInstance register();
    }

    @GET
    public TemplateInstance getRegisterPage(){
        return Template.register();
    }
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Operation(
        summary = "Legt einen neues Benutzer konto an.",
        description = "Erstellt ein neues Benutzerkonto anhand der Daten im DTO."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.TEXT_HTML)
            )
        }
    )
    public Response addUser(NutzerFormDTO nutzerdatenForm) {
        NutzerDTO nutzerdaten = new NutzerDTO(nutzerdatenForm.vorname, nutzerdatenForm.nachname, nutzerdatenForm.email, nutzerdatenForm.benutzername, nutzerdatenForm.passwort);
       if(this.benutzerverwaltung.legeBenutzerkontoAn(nutzerdaten)){
        return null;
       }
       return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    
}
