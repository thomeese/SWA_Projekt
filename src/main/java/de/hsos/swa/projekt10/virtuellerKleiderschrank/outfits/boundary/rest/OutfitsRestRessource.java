package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/outfits")
@Tag(name = "Outfits")
public class OutfitsRestRessource {

    @LoggerName("out-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitsRestRessource.class);
    
    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Outfits des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Outfits aus seinem virtuellen Kleiderschrank."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = OutfitOutputDTO.class))
            )
        }
    )
    public Response getAlleOutfits() {
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - gestartet");
        //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
        List<OutfitOutputDTO> outfitDTOs = this.outfitsVerwaltung.holeAlleOutfits(this.sc.getPrincipal().getName())
            .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
        outfitLog.trace(System.currentTimeMillis() + ": getAlleOutfits-Methode - gibt alle Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - beendet");
        
        return Response.status(Response.Status.OK).entity(outfitDTOs).build();
    }

    @GET
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt ein einzelnes Outfit anhand der ID.",
        description = "Holt ein einzelnes Outfit anhand der ID, welches der eingeloggte Benutzer erstellt hat."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OutfitOutputDTO.class)
                )
            )
        }
    )
    public Response getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeOutfitById(outfitId, this.sc.getPrincipal().getName()));
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt ein Outfit anhand der Id fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
        return Response.status(Response.Status.OK).entity(outfitDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Outfit.",
        description = "Erstellt ein neues Outfit im Repository fuer den eingeloggten Benutzer mit den Daten aus dem DTO Objekt."
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
    public Response erstelleNeuesOutfit(OutfitInputDTO outfitInputDTO) {
        outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - gestartet");
        long outfitId = this.outfitsVerwaltung.erstelleOutfit(outfitInputDTO, this.sc.getPrincipal().getName());
        outfitLog.trace(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - erstellt ein neues Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - beendet");
        return Response.status(Response.Status.CREATED).entity(outfitId).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Loescht ein vorhandenes Outfit anhand der ID.",
        description = "Loescht ein zuvor vom eingeloggten Benutzer erstelltes Outfit anhand der ID aus dem Repository."
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
    public Response loescheOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - gestartet");
        if(this.outfitsVerwaltung.loescheOutfit(outfitId, this.sc.getPrincipal().getName())) {
            outfitLog.trace(System.currentTimeMillis() + ": loescheOutfit-Methode - loescht ein Outfit fuer einen Benutzer durch Rest-Ressource");
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        outfitLog.trace(System.currentTimeMillis() + ": loescheOutfit-Methode - loescht ein Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - beendet ohne das ein Outfit geloescht wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Loescht alle vorhandenen Outfits aus dem Virtuellen Kleiderschrank.",
        description = "Loescht alle zuvor vom eingeloggten Benutzer erstellten Outfits aus dem Repository."
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
    public Response loescheAlleOutfits() {
        outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - gestartet");
        if(this.outfitsVerwaltung.loescheAlleOutfits(this.sc.getPrincipal().getName())) {
            outfitLog.trace(System.currentTimeMillis() + ": loescheAlleOutfitst-Methode - loescht alle Outfits fuer einen Benutzer durch Rest-Ressource");
            outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        outfitLog.trace(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - loescht alle Outfits fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - beendet ohne das ein Outfit geloescht wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Aktualisiert das vorhandene Outfit.",
        description = "Aktualisiert das vorhandene Outfit des eingeloogten Benutzers mit den übergebenen Teildaten."
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
    public Response bearbeiteOutfit(@PathParam("id") long outfitId, OutfitInputDTO outfitInputDTO) {
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - gestartet");
        if(this.outfitsVerwaltung.bearbeiteOutfit(outfitId, outfitInputDTO, this.sc.getPrincipal().getName())) {
            outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Rest-Ressource");
            outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet ohne das ein Outfit bearbeitet wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
