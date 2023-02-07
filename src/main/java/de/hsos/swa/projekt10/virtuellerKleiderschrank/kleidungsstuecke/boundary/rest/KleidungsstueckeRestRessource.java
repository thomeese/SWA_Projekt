package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import io.quarkus.security.identity.SecurityIdentity;

import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/api/clothes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KleidungsstueckeRestRessource {

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getAlleKleidungsstuecke() {
        
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = this.kVerwaltung.holeAlleKleidungsstuecke(sc.getPrincipal().getName())
            .stream().map(kleidungsstueck -> KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(kleidungsstueck)).toList();
        
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTOs).build();
    }

    @GET
    @Path("/{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response getKleidungsstueck(@PathParam("id") long kleidungsId) {
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        KleidungsstueckOutputDTO kleidungsstueckDTO = KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(this.kVerwaltung.holeKleidungsstueckById(kleidungsId, sc.getPrincipal().getName()));
        
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response erstelleNeuesKleidungsstueck(KleidungsstueckInputDTO kleidungsDTO) {
        
        long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsDTO, sc.getPrincipal().getName());
        return Response.status(Response.Status.CREATED).entity(kleidungsId).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response loescheKleidungsstueck(@PathParam("id") long kleidungsId) {

        if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response loescheAlleKleidungsstuecke() {

        if(this.kVerwaltung.loescheAlleKleidungsstuecke(sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
    @Path("{id}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    public Response bearbeiteKleidungsstueck(@PathParam("id") long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO) {
        
        if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
