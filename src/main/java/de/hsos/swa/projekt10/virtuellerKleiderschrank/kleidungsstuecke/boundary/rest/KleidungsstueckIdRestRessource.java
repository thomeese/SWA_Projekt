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
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.control.KleidungstueckeProvider;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.security.identity.SecurityIdentity;
import shared.LinkBuilder;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
/**
 * Realisisert die Rest Schnittstelle fuer ein Kleidungsstueck dar, bei der die CRUD-Funktionen umgesetzt sind.
 * @author Manuel Arling
 */
public class KleidungsstueckIdRestRessource {
    @LoggerName("kl-rest-id-ressource")
    private static Logger kleidungLog = Logger.getLogger(KleidungsstueckIdRestRessource.class);

    @Inject
    private KleidungstueckeProvider kVerwaltung;

    @Inject
    private DTOKonverter dtoKonverter;

    @Inject
    LinkBuilder linkBuilder;

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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server error"
            )
        }
    )
    public Response getKleidungsstueck(@PathParam("id") long kleidungsId) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - gestartet");
            kleidungLog.trace(System.currentTimeMillis()+ ": getKleidungsstueck-Methode - gibt das Kleidungsstueck mit der Id " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource.");

            //Hole alle Kleidungsstuecke vom Benutzer und Konvertiere zu OutputDTOs
            Kleidungsstueck kleidungsstueck = this.kVerwaltung.holeKleidungsstueckById(kleidungsId, benutzername);
            KleidungsstueckOutputDTO kleidungsstueckDTO = this.dtoKonverter.konvert(kleidungsstueck);
            this.linkBuilder.addLinksZuKleidungsstueckOutputDTO(kleidungsstueckDTO, this.uriInfo);

            kleidungLog.debug(System.currentTimeMillis() + ": getKleidungsstueck-Methode - beendet");
            return Response.status(Response.Status.OK).entity(kleidungsstueckDTO).build();
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Holen eines Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } 
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )


    public Response loescheKleidungsstueck(@PathParam("id") long kleidungsId) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - gestartet");
            kleidungLog.trace(System.currentTimeMillis()+ ": loescheKleidungsstueck-Methode - loescht das Kleidungsstueck mit der Id " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource.");
   
            if(this.kVerwaltung.loescheKleidungsstueck(kleidungsId, this.sc.getPrincipal().getName())) {
                kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                kleidungLog.debug(System.currentTimeMillis() + ": loescheKleidungsstueck-Methode - beendet ohne das ein Kleidungsstueck geloescht wurde");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Loeschen eines Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } 
        
        

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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response bearbeiteKleidungsstueck(@PathParam("id") long kleidungsId, KleidungsstueckInputDTO kleidungsstueckInputDTO) {
        try {
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - gestartet");
            kleidungLog.trace(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - bearbeitet das Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource");
            if(this.kVerwaltung.bearbeiteKleidungsstueck(kleidungsId, kleidungsstueckInputDTO, this.sc.getPrincipal().getName())) {
                kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                kleidungLog.debug(System.currentTimeMillis() + ": bearbeiteKleidungsstueck-Methode - beendet ohne das Kleidungsstueck zu bearbeiten");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Bearbeiten eines Kleidungsstueckes ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } 
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response fuegeKategorieHinzu(@PathParam("id") long kleidungsId,@Valid KategorieDTO kategorie){
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": fuegeKategorieHinzu-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.trace(System.currentTimeMillis() + ": fuegeKategorieHinzu-Methode - fuegt die Kategorie " + kategorie + " zu dem Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource hinzu");
            
            if(this.kVerwaltung.fuegeKategorieHinzu(kleidungsId,kategorie, benutzername)) {
                kleidungLog.debug(System.currentTimeMillis() + ": fuegeKategorieHinzu-Methode - beendet");
                return Response.status(Response.Status.OK).build();
            } else {
                kleidungLog.debug(System.currentTimeMillis() + ": fuegeKategorieHinzu-Methode - beendet ohne eine Kategorie zu ergaenzen");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Hinzufuegen einer neuen Kategorie zu einem Kleidungsstueck ist ein Fehler aufgetreten: " + ex.getMessage());
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
            ),
            @APIResponse(
                responseCode = "500",
                description = "Internal Server Error"
            )
        }
    )
    public Response entferneKategorie(@PathParam("id") long kleidungsId,@PathParam("category") String kategorie){    
        try {
            kleidungLog.debug(System.currentTimeMillis() + ": entferneKategorie-Methode - gestartet");
            String benutzername = this.sc.getPrincipal().getName();
            kleidungLog.trace(System.currentTimeMillis() + ": entferneKategorie-Methode - entfernt die Kategorie " + kategorie + " von dem Kleidungsstueck mit der ID " + kleidungsId + " fuer den Benutzer " + benutzername + " durch Rest-Ressource hinzu");
            
            if(this.kVerwaltung.entferneKategorie(kleidungsId,kategorie, this.sc.getPrincipal().getName())) {
                return Response.status(Response.Status.OK).build();
            } else {
                kleidungLog.debug(System.currentTimeMillis() + ": entferneKategorie-Methode - beendet ohne eine Kategorie zu loeschen");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch(Exception ex) {
            kleidungLog.error(System.currentTimeMillis() + ": Beim Entfernen einer Kategorie von einer Kleidungsstueck ist ein Fehler aufgetreten: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } 
    }
}
