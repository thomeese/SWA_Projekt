package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.web;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFormDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungstueckeProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.faulttolerance.Fallback;
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
/**
 * Realisisert die Web Schnittstelle fuer die Interaktion mit allen Kleidungsstuecken dar.
 * @author Manuel Arling
 */
public class KleidungsstueckWebRessource {
    @LoggerName("kl-web-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckWebRessource.class);

    @Inject
    private KleidungstueckeProvider kVerwaltung;

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
    @Fallback(
        fallbackMethod = "fallbackGetAlleKleidungsstuecke"
    )
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
        String benutzername = this.sc.getPrincipal().getName();
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        List<Kleidungsstueck> kleidungsstuecke= this.kVerwaltung.holeAlleKleidungsstuecke(filter, benutzername);
        List<KleidungsstueckOutputDTO> kleidungsstueckDTOs = kleidungsstuecke.stream().map(kleidungsstueck -> this.dtoKonverter.konvert(kleidungsstueck)).toList();
        kleidungLog.trace(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - gibt alle Kleidungsstuecke fuer den Benutzer " + benutzername + " durch Web-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": getAlleKleidungsstuecke-Methode - beendet");
        return Templates.general(kleidungsstueckDTOs);
    }

    public TemplateInstance fallbackGetAlleKleidungsstuecke(KleidungsstueckFilter filter) {
        return Templates.general(new ArrayList<KleidungsstueckOutputDTO>());
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
                responseCode = "303",
                description = "See Other"
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response erstelleNeuesKleidungsstueck(@Context UriInfo uriInfo, KleidungsstueckFormDTO kleidungsstueckFormDTO) {
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();

            kleidungLog.trace(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - erstellt ein neues Kleidungsstuecke fuer den Benutzer " + benutzername + " durch Web-Ressource");
            KleidungsstueckInputDTO kleidungsstueckInputDTO = dtoKonverter.konvert(kleidungsstueckFormDTO);
            long kleidungsId = this.kVerwaltung.erstelleKleidungsstueck(kleidungsstueckInputDTO, benutzername);
            kleidungLog.debug(System.currentTimeMillis() + ": erstelleNeuesKleidungsstueck-Methode - beendet");
            return Response.seeOther(uriInfo.getRequestUriBuilder().path(String.valueOf(kleidungsId)).build()).build();
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Erstellen eines neuen Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
