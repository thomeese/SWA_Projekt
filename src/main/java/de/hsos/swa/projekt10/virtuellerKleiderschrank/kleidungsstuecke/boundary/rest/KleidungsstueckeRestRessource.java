package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.KleidungsstueckVonOnlineHaendler;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.core.cli.annotations.Description;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;



@Path("/api/clothes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Kleidungsstuecke")
public class KleidungsstueckeRestRessource {

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private KleidungsstueckVonOnlineHaendler kleidungsstueckVonOnlineHaendler;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Kleidungsstuecke des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Kleidungsstuecke aus seinem virtuellen Kleiderschrank."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = KleidungsstueckOutputDTO.class))
            )
        }
    )
    public Response getAlleKleidungsstuecke() {
        
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = this.kVerwaltung.holeAlleKleidungsstuecke(this.sc.getPrincipal().getName())
            .stream().map(kleidungsstueck -> KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(kleidungsstueck)).toList();
        
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTOs).build();
    }

    @GET
    @Path("/{id}")
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
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        KleidungsstueckOutputDTO kleidungsstueckDTO = KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(this.kVerwaltung.holeKleidungsstueckById(kleidungsId, sc.getPrincipal().getName()));
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Kleidungsstueck.",
        description = "Erstellt ein neues Kleidungsstueck im Repository fuer den eingeloggten Benutzer mit den Daten aus dem DTO Objekt."
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
    public Response erstelleNeuesKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO) {
        //TODO eventuell erstelltes Objekt zurueckgeben
        long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsDTO, this.sc.getPrincipal().getName());
        return Response.status(Response.Status.CREATED).entity(kleidungsId).build();
    }

    @DELETE
    @Path("{id}")
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

        if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, this.sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Loescht alle vorhandenen Kleidungsstuecke aus dem Virtuellen Kleiderschrank.",
        description = "Loescht alle zuvor vom eingeloggten Benutzer erstellten Kleidungsstuecke aus dem Repository."
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
    public Response loescheAlleKleidungsstuecke() {

        if(this.kVerwaltung.loescheAlleKleidungsstuecke(this.sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
    @Path("{id}")
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
        //TODO eventuell aktualisiertes Objekt zurueckgeben
        if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, this.sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
