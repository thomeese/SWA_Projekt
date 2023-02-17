package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;


import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformation;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformationsDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/outfits/shared/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
/**
 * Realisisert die Rest Schnittstelle fuer das Setzen des Teile-Status eines Outfits.
 * @author Manuel Arling
 */
public class OutfitShardIdRestRessource {

    @LoggerName("out-rest-shared-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitShardIdRestRessource.class);
    
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
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OutfitOutputDTO.class)
                )
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response getOutfit(@PathParam("id") long outfitId) {
        try {
            outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
            outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt das Outfit mit der Id " +outfitId + " zurueck, sofern dieses als geteilt makiert ist");

            OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeGeteiltesOutfit(outfitId));
            List<KleidungsstueckInformationsDTO> kleidungsstuecke = new ArrayList<KleidungsstueckInformationsDTO>();
                for(int index = 0; index < outfitDTO.kleidungsstuecke.size(); index++){
                    kleidungsstuecke.add(this.kInformation.gebeKleidungsstueckInforamtionen(outfitDTO.kleidungsstuecke.get(index)));
                }
            outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
         return Response.status(Response.Status.OK).entity(outfitDTO).build();
        } catch(Exception ex) {
            outfitLog.error(System.currentTimeMillis() + ": Beim Holen eines geteilten Outfits ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
