package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.NutzerDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.control.Benutzerverwaltung;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/register")
public class BenutzerRestRessource {
    @Inject 
    Benutzerverwaltung benutzerverwaltung;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Legt einen neues Benutzer konto an.",
        description = "Erstellt ein neues Benutzerkonto anhand der Daten im DTO."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "201",
                description = "CREATED",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
        }
    )
    /**
     * Erstellt ein neues Benutzerkonto anhand der Daten im DTO.
     * 
     * @param nutzerdaten Fuer die Erstellung benoetigte Nutzerdaten
     * @return Respose Statuscode 201 bei erfolgt
     */
    public Response addUser(NutzerDTO nutzerdaten) {
       if(this.benutzerverwaltung.legeBenutzerkontoAn(nutzerdaten)){
        return Response.status(Status.CREATED).build();
       }
       return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

}