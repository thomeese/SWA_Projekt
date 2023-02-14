package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformation;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformationsDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;


@Path("/web/outfits/{id}")
public class OutfitIdWebRessource {

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance detail(OutfitOutputDTO outfit, List<KleidungsstueckInformationsDTO> infos);
    }

    @LoggerName("out-web-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitIdRestRessource.class);
    
    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

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
    public TemplateInstance getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeOutfitById(outfitId, this.sc.getPrincipal().getName()));
        List<KleidungsstueckInformationsDTO> kleidungsstuecke = new ArrayList<KleidungsstueckInformationsDTO>();
        for(int index = 0; index < outfitDTO.kleidungsstuecke.size(); index++ ){
            kleidungsstuecke.add(this.kInformation.gebeKleidungsstueckInforamtionen(outfitDTO.kleidungsstuecke.get(index)));
        }
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt ein Outfit anhand der Id fuer einen Benutzer durch Rest-Ressource");
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
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
        }
    )
    public Response bearbeiteOutfit(@PathParam("id") long outfitId, OutfitInputDTO outfitInputDTO) {
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - gestartet");
        if(this.outfitsVerwaltung.bearbeiteOutfit(outfitId, outfitInputDTO, this.sc.getPrincipal().getName())) {
            outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Web-Id-Put-Ressource");
            outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet");
            return Response.status(Response.Status.RESET_CONTENT).build();
        }
        outfitLog.trace(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - bearbeitet ein Outfit fuer einen Benutzer durch Web-Id-Put-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": bearbeiteOutfit-Methode - beendet ohne das ein Outfit bearbeitet wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
