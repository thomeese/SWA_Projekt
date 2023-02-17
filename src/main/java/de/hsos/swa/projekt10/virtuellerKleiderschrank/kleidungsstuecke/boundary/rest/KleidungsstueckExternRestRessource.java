package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckExternInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/clothes/extern")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

/**
 * Realisisert die Rest Schnittstelle fuer das Erstellen eines Kleidungsstuecks durch eine externe API dar.
 * 
 * @author Manuel Arling
 */
public class KleidungsstueckExternRestRessource {
    @LoggerName("kl-rest-extern-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckExternRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    SecurityIdentity sc;

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Kleidungsstueck anhand der Artikelnummer und des Online Haendlers.",
        description = "Erstellt ein neues Kleidungsstueck anhand der Daten im DTO durch die Verwendung einer externen API des Online Haendlers."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                responseCode = "400",
                description = "Bad Request"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response erstelleNeuesKleidungsstueckMitExterneAPI(@Valid KleidungsstueckExternInputDTO kleidungsstueckExternInputDTO) {
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - erstellt ein neues Kleidungsstueck fuer den Benutzer " + benutzername + " durch den Haendlernamen Rest-Ressource");

            long kleidungsId = this.kVerwaltung.erstelleKleidungsstueckMitExterneApi(kleidungsstueckExternInputDTO, benutzername);
            
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - beendet");
            return Response.status(Response.Status.OK).entity(kleidungsId).build();
        } catch (ExterneAPIException ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstueckes durch eine externe Api ist ein Fehler aufgetreten: " + ex.getMessage());
            if(ex.getErrorCode() == 2) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } catch (Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstuecks mittels Haendlerinfo ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
