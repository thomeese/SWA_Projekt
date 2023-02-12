package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
public class KleidungsstueckRestRessource {
    @LoggerName("kl-rest-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;

    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Kleidungsstuecke des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Kleidungsstuecke aus seinem virtuellen Kleiderschrank."
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
    public Response getAlleKleidungsstuecke(KleidungsstueckFilter filter) {
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gestartet");
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<Kleidungsstueck> kleidungsstuecke= this.kVerwaltung.holeAlleKleidungsstuecke(filter, this.sc.getPrincipal().getName());
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = kleidungsstuecke.stream().map(kleidungsstueck -> this.dtoKonverter.konvert(kleidungsstueck)).toList();
        kleidungLog.trace(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gibt alle Kleidungsstuecke fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - beendet");

        return Response.status(Response.Status.OK).entity(kleidungsstueckDTOs).build();
    }

    
    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    //@RolesAllowed("benutzer")
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
            )
        }
    )
    public Response erstelleNeuesKleidungsstueck(@Valid KleidungsstueckInputDTO kleidungsDTO) {
        //TODO eventuell erstelltes Objekt zurueckgeben
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - gestartet");
        long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsDTO, "gustav");//this.sc.getPrincipal().getName());
        kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - erstellt ein neues Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - beendet");
        return Response.status(Response.Status.CREATED).entity(kleidungsId).build();
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
            )
        }
    )
    public Response loescheAlleKleidungsstuecke() {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - gestartet");
        if(this.kVerwaltung.loescheAlleKleidungsstuecke(this.sc.getPrincipal().getName())) {
            kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - loescht alle Kleidungsstuecke fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheAlleKleidungsstuecke-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
