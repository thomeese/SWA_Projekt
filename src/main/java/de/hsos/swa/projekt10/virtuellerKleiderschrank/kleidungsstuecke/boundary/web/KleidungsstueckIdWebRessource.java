package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;


@Path("/web/clothes/{id}")
/**
 * Realisisert die Web Schnittstelle fuer ein Kleidungsstueck dar, bei der die CRUD-Funktionen umgesetzt sind.
 * @author Manuel Arling
 */
public class KleidungsstueckIdWebRessource {
    @LoggerName("kl-web-id-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckIdWebRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;

    @CheckedTemplate
    public static class Template {
        public static native TemplateInstance detail(KleidungsstueckOutputDTO kleidungsstueck);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed("benutzer")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Operation(
        summary = "Holt ein einzelnes Kleidungsstueck anhand der ID.",
        description = "Holt ein einzelnes Kleidungsstueck anhand der ID, welches der eingeloggte Benutzer erstellt hat."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    mediaType = MediaType.TEXT_HTML
                )
            )
        }
    )
    public TemplateInstance getKleidungsstueck(@PathParam("id") long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gestartet");
        String benutzername = this.sc.getPrincipal().getName();
        kleidungLog.trace(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gibt das Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Web-Ressource");

        //Hole alle Kleidungsstuecke vom Benutzer und Konvertiere zu OutputDTOs
        KleidungsstueckOutputDTO kleidungsstueckDTO = this.dtoKonverter.konvert(this.kVerwaltung.holeKleidungsstueckById(kleidungsId, benutzername));
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - beendet");
        return Template.detail(kleidungsstueckDTO);
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")

    @Operation(
        summary = "Loescht ein vorhandenes Kleidungsstueck anhand der ID.",
        description = "Loescht ein zuvor vom eingeloggten Benutzer erstelltes Kleidungsstueck anhand der ID aus dem Repository."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response loescheKleidungsstueck(@PathParam("id") long kleidungsId) {
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, benutzername)) {
                kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - loescht das Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Web-Ressource");
                kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Loeschen eines neuen Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        
        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Aktualisiert das vorhandene Kleidungsstueck.",
        description = "Aktualisiert das vorhandene Kleidungsstueck des eingeloogten Benutzers mit den übergebenen Teildaten."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response bearbeiteKleidungsstueck(@PathParam("id") long kleidungsId,@Valid KleidungsstueckInputDTO kleidungsstueckInputDTO) {
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, benutzername)) {
                kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet das Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Web-Ressource");
                kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {           
                kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck bearbeitet wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Bearbeiten eines neuen Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
