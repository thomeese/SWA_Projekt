package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformation;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL.KleidungsstueckInformationsDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/outfits/shared/{id}")
public class OutfitShardIdRestRessource {

    @LoggerName("out-rest-shared-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitShardIdRestRessource.class);
    
    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

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
    public Response getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeGeteiltesOutfit(outfitId));
        List<KleidungsstueckInformationsDTO> kleidungsstuecke = new ArrayList<KleidungsstueckInformationsDTO>();
        for(int index = 0; index < outfitDTO.kleidungsstuecke.size(); index++){
            kleidungsstuecke.add(this.kInformation.gebeKleidungsstueckInforamtionen(outfitDTO.kleidungsstuecke.get(index), this.sc.getPrincipal().getName()));
        }
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt ein Outfit anhand der Id zurueck, sofern dieses als geteilt makiert ist");
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
        return Response.status(Response.Status.OK).entity(outfitDTO).build();
    }
}
