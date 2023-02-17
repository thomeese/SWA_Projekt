package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformation;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformationsDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/web/outfits/shared/{id}")
@ApplicationScoped
/**
 * Realisisert die Web Schnittstelle fuer das Interagieren mit einem geteilten Outfit.
 * @author Manuel Arling
 */
public class OutfitSharedIdWebRessource {
    
    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance shared(OutfitOutputDTO outfit, List<KleidungsstueckInformationsDTO> infos);
    }

    @LoggerName("out-shared-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitSharedIdWebRessource.class);
    
    @Inject
    private OutfitProvider outfitsVerwaltung;

    @Inject
    KleidungsstueckInformation kInformation;

    @Inject
    SecurityIdentity sc;


    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Operation(
        summary = "Holt ein einzelnes geteiltes Outfit anhand der ID.",
        description = "Holt ein einzelnes Outfit anhand der ID, welches vom erstellten Benutzer als geteilt makiert wurde."
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
     * @author Manuel Arling
     */
    public TemplateInstance getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt das Outfit mit der Id " +outfitId + " zurueck, sofern dieses als geteilt makiert ist");

        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeGeteiltesOutfit(outfitId));
        List<KleidungsstueckInformationsDTO> kleidungsstuecke = new ArrayList<KleidungsstueckInformationsDTO>();
        for(int index = 0; index < outfitDTO.kleidungsstuecke.size(); index++){
            kleidungsstuecke.add(this.kInformation.gebeKleidungsstueckInforamtionen(outfitDTO.kleidungsstuecke.get(index)));
        }

        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
        return Templates.shared(outfitDTO, kleidungsstuecke);
    }
}
