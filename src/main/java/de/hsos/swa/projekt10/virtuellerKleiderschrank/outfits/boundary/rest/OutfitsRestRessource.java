package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutiftsVerwaltung;

@Path("/api/outfits")
public class OutfitsRestRessource {
    @Inject
    OutiftsVerwaltung outiftsVerwaltung;

    //TODO Authentication ergaenzen. Nutzername wird erstmal gemockt
    String benutzername = "Gustav";

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getAlleOutfits() {

        //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
        List<OutfitOutputDTO> outfitDTOs = this.outiftsVerwaltung.holeAlleOutfits(benutzername)
            .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
        
        
        return Response.status(Response.Status.OK).entity(outfitDTOs).build();
    }

    @GET
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getOutfit(@PathParam("id") long outfitId) {

        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outiftsVerwaltung.holeOutfit(outfitId, benutzername));

        return Response.status(Response.Status.OK).entity(outfitDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response erstelleNeuesOutfit(OutfitInputDTO outfitInputDTO) {
     
        if(this.outiftsVerwaltung.erstelleOutfit(outfitInputDTO, benutzername)) {
            return Response.status(Response.Status.CREATED).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
