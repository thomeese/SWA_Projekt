package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckExternFormDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckExternInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/web/clothes/extern")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_HTML)
public class KleidungsstueckExternWebRessource {
    @LoggerName("kl-web-extern-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckExternWebRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;
    
    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Kleidungsstueck anhand der Artikelnummer und des Online Haendlers.",
        description = "Erstellt ein neues Kleidungsstueck anhand der Daten im DTO durch die Verwendung einer externen API des Online Haendlers."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "303",
                description = "See Other"
            ),
            @APIResponse(
                responseCode = "400",
                description = "Bad Request"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response erstelleNeuesKleidungsstueckMitExterneAPI(@Context UriInfo uriInfo,KleidungsstueckExternFormDTO kleidungsstueckExternFormInputDTO) throws ExterneAPIException {
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - erstellt ein neues Kleidungsstueck fuer den Benutzer " + benutzername + " durch den Haendlernamen Web-Ressource");

            KleidungsstueckExternInputDTO kleidungsstueckExternInputDTO = this.dtoKonverter.konvert(kleidungsstueckExternFormInputDTO);
            long kleidungsId = this.kVerwaltung.erstelleKleidungsstueckMitExterneApi(kleidungsstueckExternInputDTO, benutzername);

            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueckMitExterneAPI-Methode - beendet");
            return Response.seeOther(uriInfo.getBaseUriBuilder().path(KleidungsstueckIdWebRessource.class).build(String.valueOf(kleidungsId))).build();
            } catch (ExterneAPIException ex) {
                kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstueckes durch eine externe Api ist ein Fehler aufgetreten: " + ex.getMessage());
                if(ex.getErrorCode() == 2) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
                }
            } catch (Exception ex) {
                kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstueckes durch eine externe Api ist ein Fehler aufgetreten: " + ex.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }   
    }
}
