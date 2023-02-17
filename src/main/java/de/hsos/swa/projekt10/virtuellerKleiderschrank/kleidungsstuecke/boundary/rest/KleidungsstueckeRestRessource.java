package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckListeOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;
import shared.RessourceUriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;



@Path("/api/clothes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Kleidungsstuecke")
public class KleidungsstueckeRestRessource {
    @LoggerName("kl-rest-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckeRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;

    @Inject
    RessourceUriBuilder uriBuilder;

    @Context
    UriInfo uriInfo;


    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Fallback(
        fallbackMethod = "fallbackGetAlleKleidungsstuecke"
    )
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Kleidungsstuecke des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellten Kleidungsstuecke aus seinem virtuellen Kleiderschrank mit den gesetzen Filtern."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                        schema = @Schema(type= SchemaType.ARRAY,
                                                    implementation = KleidungsstueckListeOutputDTO.class))
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response getAlleKleidungsstuecke(KleidungsstueckFilter filter) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gestartet");
            kleidungLog.trace(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gibt alle Kleidungsstuecke fuer den Benutzer " + benutzername + " durch die Rest-Ressource zurueck.");
            List<Kleidungsstueck> kleidungsstuecke = this.kVerwaltung.holeAlleKleidungsstuecke(filter, benutzername);
            //Konvertiere Kleidungsstuecke zu DTOs und ergänze HATEOAS Links
            List<KleidungsstueckOutputDTO> kleidungsstueckeDTO = kleidungsstuecke.stream()
                .map(kleidungsstueck -> this.dtoKonverter.konvert(kleidungsstueck))
                .peek(this::addSelfLinkZuKleidungsstueckInListeOutputDTO)
                .toList();
            KleidungsstueckListeOutputDTO kleidungsstueckListeOutputDTO = new KleidungsstueckListeOutputDTO(kleidungsstueckeDTO);
            this.addSelfLinkZuKleidungsstueckeListeOutputDTO(kleidungsstueckListeOutputDTO);

            kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - beendet");
            return Response.status(Response.Status.OK).entity(kleidungsstueckListeOutputDTO).build();
        } catch (Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Geben aller Kleidungsstuecke eines Benutzers ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Response fallbackGetAlleKleidungsstuecke(KleidungsstueckFilter filter) {
        return Response.status(Response.Status.OK).entity(new ArrayList<KleidungsstueckOutputDTO>()).build();
    }
    
    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Kleidungsstueck.",
        description = "Erstellt ein neues Kleidungsstueck im Repository fuer den eingeloggten Benutzer mit den Daten aus dem DTO Objekt."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "201",
                description = "CREATED",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response erstelleNeuesKleidungsstueck(@Valid KleidungsstueckInputDTO kleidungsDTO) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - gestartet");
            kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - erstellt ein neues Kleidungsstueck fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsDTO, benutzername);
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.CREATED).entity(kleidungsId).build();
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Loescht alle vorhandenen Kleidungsstuecke aus dem Virtuellen Kleiderschrank.",
        description = "Loescht alle zuvor vom eingeloggten Benutzer erstellten Kleidungsstuecke aus dem Repository."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(mediaType = MediaType.APPLICATION_JSON)
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response loescheAlleKleidungsstuecke() {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - gestartet");
        kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - loescht alle Kleidungsstuecke fuer den Benutzer: " + benutzername + " durch Rest-Ressource");

        if(this.kVerwaltung.loescheAlleKleidungsstuecke(benutzername)) {
            kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        } else {
            kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim loeschen aller Kleidungsstuecke vom Benutzer ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }



    private void addSelfLinkZuKleidungsstueckInListeOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO) {
        URI selfUri = this.uriBuilder.fuerKleidungsstueck(kleidungsstueckOutputDTO.kleidungsId, this.uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel("self")
                        .type(MediaType.APPLICATION_JSON)
                        .param("get kleidungsstueck", "GET")
                        .param("change kleidungsstueck", "PATCH")
                        .param("delete kleidungsstueck", "DELETE")
                        .build();
        kleidungsstueckOutputDTO.addLink("self", link);
    }

    private void addSelfLinkZuKleidungsstueckeListeOutputDTO(KleidungsstueckListeOutputDTO kleidungsstueckListeOutputDTO) {
        URI selfUri = this.uriBuilder.fuerKleidungsstuecke(this.uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel("self")
                        .type(MediaType.APPLICATION_JSON)
                        .param("get alle kleidungsstuecke", "GET")
                        .param("post erstelle neues kleidungsstueck", "POST")
                        .param("delete alle kleidungsstuecke", "DELETE")
                        .build();
                        kleidungsstueckListeOutputDTO.addLink("self", link);
    }
}
