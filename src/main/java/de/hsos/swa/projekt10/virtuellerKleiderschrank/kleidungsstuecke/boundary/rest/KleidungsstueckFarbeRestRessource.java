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

import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;

@Path("/api/clothes/color/{color}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KleidungsstueckFarbeRestRessource {
    @LoggerName("kl-farbe-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckFarbeRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Kleidungsstuecke anhand der Farbe.",
        description = "Holt alle vom Benutzer erstellen Kleidungsstuecke, welche die Farbe gesetzt haben."
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
    public Response holeAlleKleidungsstueckeMitFarbe(@PathParam("color") Farbe farbe) {
        List<Kleidungsstueck> kleidungsstueckeMitFarbe = this.kVerwaltung.filterNachFarbe(farbe, sc.getPrincipal().getName());
        List<KleidungsstueckOutputDTO> kleidungsstueckMitFarbeDTOs = kleidungsstueckeMitFarbe.stream().map(kleidungsstueck -> KleidungsstueckOutputDTO.Converter.toKleidungsstueckOutputDTO(kleidungsstueck)).toList();

        return Response.status(Response.Status.OK).entity(kleidungsstueckMitFarbeDTOs).build();
    }
}
