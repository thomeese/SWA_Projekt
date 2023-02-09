package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
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
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;


@Path("/api/clothes/name/{name}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KleidungsstueckNameRestRessource {
    @LoggerName("kl-name-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckNameRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Kleidungsstuecke mit dem Namen.",
        description = "Holt alle vom Benutzer erstellen Kleidungsstuecke die den Namen besitzen."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                                    schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = KleidungsstueckOutputDTO.class))
            )
        }
    )
    public Response holeAlleKleidungsstueckeMitName(@PathParam("name") String name) {
        List<Kleidungsstueck> kleidungsstueckeMitName = this.kVerwaltung.filterNachName(name, sc.getPrincipal().getName());
        List<KleidungsstueckOutputDTO> kleidungsstueckeMitNameDTOs = kleidungsstueckeMitName.stream().map(kleidungsstueck -> KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(kleidungsstueck)).toList();

        return Response.status(Response.Status.OK).entity(kleidungsstueckeMitNameDTOs).build();
    }
}