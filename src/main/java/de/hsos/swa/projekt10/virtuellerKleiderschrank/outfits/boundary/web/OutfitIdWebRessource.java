package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformation;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformationsDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitTeilenDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;


@Path("/web/outfits/{id}")
/**
 * Realisisert die Web Schnittstelle fuer die Interaktion mit einem Outfit und bietet CRUD-Funktionen an.
 * @author Manuel Arling
 */
public class OutfitIdWebRessource {

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance detail(OutfitOutputDTO outfit, List<KleidungsstueckInformationsDTO> infos);
    }

    @LoggerName("out-web-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitIdRestRessource.class);
    
    @Inject
    private OutfitProvider outfitsVerwaltung;

    @Inject
    KleidungsstueckInformation kInformation;

    @Inject
    SecurityIdentity sc;


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
                content = @Content(mediaType = MediaType.TEXT_HTML)
            )
        }
    )
    /**
     * 
     * @param outfitId
     * @return
     * @author Thomas Meese
     */
    public TemplateInstance getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        String benutzername = this.sc.getPrincipal().getName();
        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeOutfitById(outfitId, benutzername));
        List<KleidungsstueckInformationsDTO> kleidungsstuecke = new ArrayList<KleidungsstueckInformationsDTO>();
        for(int index = 0; index < outfitDTO.kleidungsstuecke.size(); index++ ){
            kleidungsstuecke.add(this.kInformation.gebeKleidungsstueckInforamtionen(outfitDTO.kleidungsstuecke.get(index)));
        }
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt das Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Web-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
        return Templates.detail(outfitDTO,kleidungsstuecke);}

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
    public Response bearbeiteOutfit(@Context UriInfo uriInfo, @PathParam("id") long outfitId,@Valid OutfitInputDTO outfitInputDTO) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet das Outfit mit der Id " + outfitId + " fuer den Benutzer " + benutzername + " durch Web-Ressource");
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
    @Path("/share")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Ändert den Teilen-Status des Outfits.",
        description = "Ändert den Teilen-Status des vom Benutzer erstellen Outfits, auf den gewuenschten Status."
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
    /**
     * 
     * @param outfitId
     * @param dto
     * @author Manuel Arling
     * @return
     */
    public Response teileOutfit(@PathParam("id") long outfitId,@Valid OutfitTeilenDTO dto){
        try {
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - für das Outfit mit der ID " + outfitId + " gestartet.");
            outfitLog.trace(System.currentTimeMillis() + ": teileOutfit-Methode - aendert den Teile-Status des Outfits mit der ID " + outfitId + " fuer den Benutzer " + benutzername + " durch die Web-Ressource.");
            if(this.outfitsVerwaltung.teileOutfit(outfitId, dto, benutzername)){
                outfitLog.info(System.currentTimeMillis() + ": der Teile-Status des Outfits mit der ID " + outfitId + " wurde erfolgreich geaendert.");
                outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - erfolgreich beendet.");
                return Response.status(Response.Status.OK).build();
            }
            outfitLog.debug(System.currentTimeMillis() + ": teileOutfit-Methode - für das Outfit mit der ID " + outfitId + " beendet ohne das der Teile-Status des Outfits veraendert wurde.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Teilen des Outfits mit der ID " + outfitId + " ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
