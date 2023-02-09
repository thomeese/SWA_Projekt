package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

@Path("/api/clothes/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KleidungsstueckIdRestRessource {
    @LoggerName("kl-id-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckIdRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
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
                    mediaType = "application/json",
                    schema = @Schema(implementation = KleidungsstueckOutputDTO.class)
                )
            )
        }
    )
    public Response getKleidungsstueck(@PathParam("id") long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gestartet");
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        KleidungsstueckOutputDTO kleidungsstueckDTO = KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(this.kVerwaltung.holeKleidungsstueckById(kleidungsId, sc.getPrincipal().getName()));
        kleidungLog.trace(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gibt ein Kleidungsstueck nach Id fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - beendet");
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
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
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
        }
    )
    public Response loescheKleidungsstueck(@PathParam("id") long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - gestartet");
        if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, this.sc.getPrincipal().getName())) {
            kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
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
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
        }
    )
    public Response bearbeiteKleidungsstueck(@PathParam("id") long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO) {
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - gestartet");
        //TODO eventuell aktualisiertes Objekt zurueckgeben
        if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, this.sc.getPrincipal().getName())) {
            kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck bearbeitet wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
