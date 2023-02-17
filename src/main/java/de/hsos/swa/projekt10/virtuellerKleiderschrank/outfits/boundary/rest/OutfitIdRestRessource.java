package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitTeilenDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;
import shared.LinkBuilder;

@Path("/api/outfits/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
/**
 * Realisisert die Rest Schnittstelle fuer die Interaktion mit einem Outfit und bietet CRUD-Funktionen an.
 * @author Manuel Arling
 */
public class OutfitIdRestRessource {
    @LoggerName("out-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitIdRestRessource.class);
    
    @Inject
    private OutfitProvider outfitsVerwaltung;

    @Inject
    SecurityIdentity sc;

    @Inject
    LinkBuilder linkBuilder;

    @Inject
    UriInfo uriInfo;


    @GET
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            ),
            @APIResponse(
                responseCode = "400",
                description = "Bad Request"
            )
        }
    )
    public Response getOutfit(@PathParam("id") long outfitId) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
            outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt das Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            
            Outfit outfit = this.outfitsVerwaltung.holeOutfitById(outfitId, benutzername);
            if(outfit == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Das Outfit konnte nicht gefunden werden").build(); 
            }
            OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit);
            outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
            this.linkBuilder.addLinksZuOutfitOutputDTO(outfitDTO, this.uriInfo);
            return Response.status(Response.Status.OK).entity(outfitDTO).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Holen eines Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response loescheOutfit(@PathParam("id") long outfitId) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            if(this.outfitsVerwaltung.loescheOutfit(outfitId, benutzername)) {
                outfitLog.trace(System.currentTimeMillis() + ": loescheOutfit-Methode - loescht das Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
                outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                outfitLog.debug(System.currentTimeMillis() + ": loescheOutfit-Methode - beendet ohne das ein Outfit geloescht wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Loeschen eines Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        
    }

    @DELETE
    @Path("/{kleidungsId}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Entfernt ein Kleidungsstueck aus dem Outfit anhand der ID.",
        description = "Entfernt ein zuvor vom eingeloggten Benutzer zu einem Outfit hinzugefuegtes Kleidungssteuck anhand der ID aus dem Repository."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response entferneKleidungsstueck(@PathParam("id") long outfitId,@PathParam("kleidungsId") long kleidungsId){
        try {
            outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueck-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": entferneKleidungsstueck-Methode - entfernt das Kleidungsstueck mit der ID " + kleidungsId + " aus dem Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            if(this.outfitsVerwaltung.entferneKleidungsstueckVonOutfit(kleidungsId, outfitId, benutzername)){
                outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueck-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                outfitLog.debug(System.currentTimeMillis() + ": entferneKleidungsstueck-Methode - beendet ohne das ein Kleidugnsstueck entfernt wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Entfernen eines Kleidungsstueckes von einem Outfit ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        
       
        
    }

    @PATCH
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response bearbeiteOutfit(@PathParam("id") long outfitId,@Valid OutfitInputDTO outfitInputDTO) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet das Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            if(this.outfitsVerwaltung.bearbeiteOutfit(outfitId, outfitInputDTO, benutzername)) {
                outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet ohne das ein Outfit bearbeitet wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Bearbeiten eines Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Teilt das vorhandene Outfit.",
        description = "Teiltt das vorhandene Outfit des eingeloogten Benutzers, sodass andere Verwender das Outfit einsehen koennen."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response teileOutfit(@PathParam("id") long outfitId,@Valid OutfitTeilenDTO dto){
        try {
            outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": teileOutfit-Methode - Setzt des Teile-Status von dem Outfit fuer einen Benutzer durch Rest-Ressource");
        if(this.outfitsVerwaltung.teileOutfit(outfitId, dto, benutzername)){
            outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        } else {
            outfitLog.trace(System.currentTimeMillis() + ": teileOutfit-Methode - Setzt des Teile-Status von dem Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - beendet ohne das der Teile-Status eines Outfit  geaendert wurde");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Setzen des Teile-Status ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}
