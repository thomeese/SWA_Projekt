package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.web;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFormDTO;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;



@Path("/web/clothes")

@Tag(name = "Kleidungsstuecke")
public class KleidungsstueckWebRessource {
    @LoggerName("kl-web-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckWebRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    SecurityIdentity sc;

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance general(List<KleidungsstueckOutputDTO> kleidungsstuecke);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
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
                content = @Content(mediaType = MediaType.TEXT_HTML)
            )
        }
    )
    public TemplateInstance getAlleKleidungsstuecke(KleidungsstueckFilter filter) {
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gestartet");
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<Kleidungsstueck> kleidungsstuecke= this.kVerwaltung.holeAlleKleidungsstuecke(filter, this.sc.getPrincipal().getName());
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = kleidungsstuecke.stream().map(kleidungsstueck -> this.dtoKonverter.konvert(kleidungsstueck)).toList();
        kleidungLog.trace(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gibt alle Kleidungsstuecke fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - beendet");

        return Templates.general(kleidungsstueckDTOs);
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
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
                content = @Content(mediaType = MediaType.TEXT_HTML)
            )
        }
    )
    public Response erstelleNeuesKleidungsstueck(@Context UriInfo uriInfo, KleidungsstueckFormDTO kleidungsstueckFormDTO) {
        //TODO eventuell erstelltes Objekt zurueckgeben
        KleidungsstueckInputDTO kleidungsstueckInputDTO = dtoKonverter.konvert(kleidungsstueckFormDTO);
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - gestartet");
        long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsstueckInputDTO, this.sc.getPrincipal().getName());
        kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - erstellt ein neues Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - beendet");
        
        return Response.seeOther(uriInfo.getRequestUriBuilder().path(String.valueOf(kleidungsId)).build()).build();
    }
}
