package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungsstueckeVerwaltung;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;
import shared.RessourceUriBuilder;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.jboss.logging.Logger;

import java.net.URI;

@Path("/api/clothes/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KleidungsstueckIdRestRessource {
    @LoggerName("kl-rest-id-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckIdRestRessource.class);

    @Inject
    private KleidungsstueckeVerwaltung kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    RessourceUriBuilder uriBuilder;

    @Context
    UriInfo uriInfo;

    @Inject
    SecurityIdentity sc;

    @GET
    @RolesAllowed("benutzer")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @Operation(
        summary = "Holt ein einzelnes Kleidungsstueck anhand der ID.",
        description = "Holt ein einzelnes Kleidungsstueck anhand der ID, welches der eingeloggte Benutzer erstellt hat."
    )
    @APIResponses(
        value = {
            @APIResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = KleidungsstueckOutputDTO.class)
                )
            )
        }
    )
    public Response getKleidungsstueck(@PathParam("id") long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gestartet");
        //Hole alle Kleidungsstuecke vom Benutzer und Convertiere zu OutputDTOs
        Kleidungsstueck kleidungsstueck = this.kVerwaltung.holeKleidungsstueckById(kleidungsId, sc.getPrincipal().getName());
        KleidungsstueckOutputDTO kleidungsstueckDTO = this.dtoKonverter.konvert(kleidungsstueck);
        this.addLinksZuKleidungsstueckOutputDTO(kleidungsstueckDTO);
        
        kleidungLog.trace(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gibt ein Kleidungsstueck nach Id fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - beendet");
        return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
    }

    @DELETE
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")

    @Operation(
        summary = "Loescht ein vorhandenes Kleidungsstueck anhand der ID.",
        description = "Loescht ein zuvor vom eingeloggten Benutzer erstelltes Kleidungsstueck anhand der ID aus dem Repository."
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
    public Response loescheKleidungsstueck(@PathParam("id") long kleidungsId) {
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - gestartet");
        if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, this.sc.getPrincipal().getName())) {
            kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        kleidungLog.trace(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - loescht ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PATCH
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Aktualisiert das vorhandene Kleidungsstueck.",
        description = "Aktualisiert das vorhandene Kleidungsstueck des eingeloogten Benutzers mit den übergebenen Teildaten."
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
    public Response bearbeiteKleidungsstueck(@PathParam("id") long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO) {
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - gestartet");
        //TODO eventuell aktualisiertes Objekt zurueckgeben
        if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, this.sc.getPrincipal().getName())) {
            kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
            kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.OK).build();
        }
        kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet ein Kleidungsstueck fuer einen Benutzer durch Rest-Ressource");
        kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck bearbeitet wurde");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }


    @POST
    @Path("/category")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Fuegt eine neue Kategorie zu dem Kleidungsstueck hinzu.",
        description = "Fuegt eine neue Kategorie zu dem Kleidungsstueck hinzu, wenn diese die Kategorie noch nicht besitzt."
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
    public Response fuegeKategorieHinzu(@PathParam("id") long kleidungsId,KategorieDTO kategorie){
        if(this.kVerwaltung.fuegeKategorieHinzu(kleidungsId,kategorie, this.sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/category/{category}")
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Loescht eine Kategorie zu dem Kleidungsstueck anhand des Namens.",
        description = "Loescht eine Kategorie zu dem Kleidungsstueck, wenn das Kleidungsstueck eine Kategorie mit dem Namen besitzt."
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
    public Response entferneKategorie(@PathParam("id") long kleidungsId,@PathParam("category") String kategorie){
        if(this.kVerwaltung.entferneKategorie(kleidungsId,kategorie, this.sc.getPrincipal().getName())) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //HATEOS Links erstellen und adden

    private void addLinksZuKleidungsstueckOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO) {
        this.addSelfLinkZuKleidungsstueckOutputDTO(kleidungsstueckOutputDTO);
        this.addCollectionZuKleidungsstueckOutputDTO(kleidungsstueckOutputDTO);
        this.addKategorieZuKleidungsstueckOutputDTO(kleidungsstueckOutputDTO);
    }

    private void addSelfLinkZuKleidungsstueckOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO) {
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

    private void addCollectionZuKleidungsstueckOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO) {
        URI collectionUri = this.uriBuilder.fuerKleidungsstuecke(this.uriInfo);
        Link link = Link.fromUri(collectionUri)
                        .rel("collection")
                        .type(MediaType.APPLICATION_JSON)
                        .param("get alle kleidungsstuecke", "GET")
                        .param("add kleidungsstueck", "POST")
                        .param("delete alle kleidungsstuecke", "DELETE")
                        .build();
        kleidungsstueckOutputDTO.addLink("collection", link);
    }

    private void addKategorieZuKleidungsstueckOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO) {
        URI addCategory = this.uriBuilder.fuerKategorieKleidungsstueck(kleidungsstueckOutputDTO.kleidungsId, "fuegeKategorieHinzu", uriInfo);
        Link link = Link.fromUri(addCategory)
                        .rel("kategorie")
                        .type(MediaType.APPLICATION_JSON)
                        .param("add kategorie", "POST")
                        .param("delete kategorie mit Namen", "DELETE")
                        .build();
        kleidungsstueckOutputDTO.addLink("kategorie", link);
    }
}
