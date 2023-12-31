package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFormDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

import java.util.ArrayList;
import java.util.List;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;
@Path("/web/outfits")
/**
 * Realisisert die Web Schnittstelle fuer die Interaktion mit allen Kleidungsstuecken dar.
 * @author Manuel Arling
 */
public class OutfitWebRessource {

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance general(List<OutfitOutputDTO> outfits);
    }

    @LoggerName("out-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitIdRestRessource.class);
    
    @Inject
    private OutfitProvider outfitsVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Fallback(
        fallbackMethod = "fallbackGetAlleOutfits"
    )
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Outfits des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Outfits aus seinem virtuellen Kleiderschrank."
    )
    /**
     * 
     * @param filter
     * @return
     * @author Thomas Meese
     */
    public TemplateInstance getAlleOutfits(OutfitFilter filter) {
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - gestartet");
        String benutzername = this.sc.getPrincipal().getName();
        outfitLog.trace(System.currentTimeMillis() + ": getAlleOutfits-Methode - gibt alle Outfit fuer den Benutzer " + benutzername + " durch Rest-Ressource");

        //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
        List<OutfitOutputDTO> outfitDTOs = this.outfitsVerwaltung.holeAlleOutfits(filter, benutzername)
            .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - beendet");
        
        return Templates.general(outfitDTOs);
    }

    public TemplateInstance fallbackGetAlleOutfits(OutfitFilter filter){
        return Templates.general(new ArrayList<OutfitOutputDTO>());
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Outfit.",
        description = "Erstellt ein neues Outfit im Repository fuer den eingeloggten Benutzer mit den Daten aus dem DTO Objekt."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "303",
                description = "See Other"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response erstelleNeuesOutfit(@Context UriInfo uriInfo, OutfitFormDTO outfitFormDTO) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            outfitLog.trace(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - erstellt ein neues Outfit fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            
            OutfitInputDTO outfitInputDTO = this.dtoKonverter.konvert(outfitFormDTO);
            long outfitId = this.outfitsVerwaltung.erstelleOutfit(outfitInputDTO, this.sc.getPrincipal().getName());
            outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - beendet");
            return Response.seeOther(uriInfo.getRequestUriBuilder().path(String.valueOf(outfitId)).build()).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Erstellen eines Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        

    }

    
}
