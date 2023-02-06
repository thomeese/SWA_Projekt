package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;



@Path("/api/clothes")
public class KleidungsstueckeRestRessource {
 
    @Inject
    KleidungsstueckeVerwaltung kVerwaltung;

    //TODO Authentication ergaenzen. Nutzername wird erstmal gemockt
    String benutzername = "Gustav";

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getAlleKleidungsstuecke() {
        
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = this.kVerwaltung.holeAlleKleidungsstuecke(benutzername)
            .stream().map(kleidungsstueck -> KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(kleidungsstueck)).toList();
        
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTOs).build();
    }

    @GET
    @Path("/{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getKleidungsstueck(@PathParam("id") long kleidungsId) {
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        KleidungsstueckOutputDTO kleidungsstueckDTO = KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(this.kVerwaltung.holeKleidungsstueckById(kleidungsId, benutzername));
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response erstelleNeuesKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO) {
        
        if(this.kVerwaltung.erstelleKleidungsstueck(kleidungsDTO, benutzername)) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
