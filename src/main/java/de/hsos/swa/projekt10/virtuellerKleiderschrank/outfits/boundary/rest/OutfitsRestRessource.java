package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

@Path("/api/outfits")
public class OutfitsRestRessource {
    @Inject
    OutfitsVerwaltung outfitsVerwaltung;

    //TODO Authentication ergaenzen. Nutzername wird erstmal gemockt
    String benutzername = "Gustav";

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getAlleOutfits() {

        //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
        List<OutfitOutputDTO> outfitDTOs = this.outfitsVerwaltung.holeAlleOutfits(benutzername)
            .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
        
        
        return Response.status(Response.Status.OK).entity(outfitDTOs).build();
    }

    @GET
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getOutfit(@PathParam("id") long outfitId) {

        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeOutfitById(outfitId, benutzername));

        return Response.status(Response.Status.OK).entity(outfitDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response erstelleNeuesOutfit(OutfitInputDTO outfitInputDTO) {
     
        long outfitId = this.outfitsVerwaltung.erstelleOutfit(outfitInputDTO, benutzername);
        return Response.status(Response.Status.CREATED).entity(outfitId).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response loescheOutfit(@PathParam("id") long outfitId) {
        if(this.outfitsVerwaltung.loescheOutfit(outfitId, benutzername)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response loescheAlleOutfits() {

        if(this.outfitsVerwaltung.loescheAlleOutfits(benutzername)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response bearbeiteOutfit(@PathParam("id") long outfitId, OutfitInputDTO outfitInputDTO) {
        
        if(this.outfitsVerwaltung.bearbeiteOutfit(outfitId, outfitInputDTO, benutzername)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
