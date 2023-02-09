package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

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
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/api/outfits/categorie/{categorie}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OutfitKategorieRestRessource {
    @LoggerName("cl-kategorie-ressource")
    private static Logger kleidungLog = Logger.getLogger(OutfitKategorieRestRessource.class);

    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Outfits anhand der Kategorie.",
        description = "Holt alle vom Benutzer erstellen Outfits, welche die Kategorie besitzen."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                                    schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = OutfitOutputDTO.class))
            )
        }
    )
    public Response holeAlleOutfitsMitKategorie(@PathParam("categorie") String kategorie) {
        List<Outfit> outfitMitKategorie = this.outfitsVerwaltung.filterNachKategorie(kategorie, sc.getPrincipal().getName());
        List<OutfitOutputDTO> outfitMitKategorieDTOs = outfitMitKategorie.stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();

        return Response.status(Response.Status.OK).entity(outfitMitKategorieDTOs).build();
    }
}
