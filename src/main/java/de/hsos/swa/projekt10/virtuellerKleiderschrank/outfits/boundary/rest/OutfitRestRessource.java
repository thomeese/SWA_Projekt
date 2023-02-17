package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/outfits")
@Tag(name = "Outfits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OutfitRestRessource {

    @LoggerName("out-rest-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitRestRessource.class);
    
    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Fallback(
        fallbackMethod = "fallbackGetAlleOutfits"
    )
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Outfits des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Outfits aus seinem virtuellen Kleiderschrank mit dem übergebenen Filter"
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = OutfitOutputDTO.class))
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response getAlleOutfits(OutfitFilter filter) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": getAlleOutfits-Methode - gibt alle Outfit fuer den Benutzer " + benutzername + " durch Rest-Ressource");

            //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
            List<OutfitOutputDTO> outfitDTOs = this.outfitsVerwaltung.holeAlleOutfits(filter, benutzername)
                .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
            outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - beendet");
            return Response.status(Response.Status.OK).entity(outfitDTOs).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Holen aller Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response fallbackGetAlleOutfits(OutfitFilter filter){
        return Response.status(Response.Status.OK).entity(new ArrayList<OutfitOutputDTO>()).build();
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response erstelleNeuesOutfit(OutfitInputDTO outfitInputDTO) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - erstellt ein neues Outfit fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            
            long outfitId = this.outfitsVerwaltung.erstelleOutfit(outfitInputDTO, this.sc.getPrincipal().getName());
            outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - beendet");
            return Response.status(Response.Status.CREATED).entity(outfitId).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response loescheAlleOutfits() {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            if(this.outfitsVerwaltung.loescheAlleOutfits(this.sc.getPrincipal().getName())) {
                outfitLog.trace(System.currentTimeMillis() + ": loescheAlleOutfitst-Methode - loescht alle Outfits fuer den Benutzer " + benutzername + " durch Rest-Ressource");
                outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                outfitLog.debug(System.currentTimeMillis() + ": loescheAlleOutfits-Methode - beendet ohne das ein Outfit geloescht wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Loeschen aller Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

       
    }
}
