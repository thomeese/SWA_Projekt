package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

import java.util.List;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;
@Path("/web/outfits")
public class OutfitWebRessource {

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance general(List<OutfitOutputDTO> outfits);
    }

    @LoggerName("out-id-ressource")
    private static Logger outfitLog = Logger.getLogger(OutfitIdRestRessource.class);
    
    @Inject
    private OutfitsVerwaltung outfitsVerwaltung;

    @Inject
    SecurityIdentity sc;


    @GET
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Holt alle Outfits des eingeloggten Benutzers.",
        description = "Holt alle vom Benutzer erstellen Outfits aus seinem virtuellen Kleiderschrank."
    )
    public TemplateInstance getAlleOutfits() {
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - gestartet");
        //Hole alle Outfits vom Benutzer und Convertiere zu OutputDTOs
        List<OutfitOutputDTO> outfitDTOs = this.outfitsVerwaltung.holeAlleOutfits(this.sc.getPrincipal().getName())
            .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();
        outfitLog.trace(System.currentTimeMillis() + ": getAlleOutfits-Methode - gibt alle Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": getAlleOutfits-Methode - beendet");
        
        return Templates.general(outfitDTOs);
    }

    @POST
    @Transactional(value = javax.transaction.Transactional.TxType.REQUIRES_NEW)
    @RolesAllowed("benutzer")
    @Operation(
        summary = "Erstellt ein neues Outfit.",
        description = "Erstellt ein neues Outfit im Repository fuer den eingeloggten Benutzer mit den Daten aus dem DTO Objekt."
    )
    public TemplateInstance erstelleNeuesOutfit(OutfitInputDTO outfitInputDTO) {
        outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - gestartet");
        long outfitId = this.outfitsVerwaltung.erstelleOutfit(outfitInputDTO, this.sc.getPrincipal().getName());
        outfitLog.trace(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - erstellt ein neues Outfit fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": erstelleNeuesOutfit-Methode - beendet");
        return Templates.general(null);
    }
}
