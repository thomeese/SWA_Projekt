package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control.OutfitsVerwaltung;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;
@Path("/web/outfits/{id}")
public class OutfitIdWebRessource {

    @CheckedTemplate
    static class Templates {
        static public native TemplateInstance detail(OutfitOutputDTO outfit);
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
        summary = "Holt ein einzelnes Outfit anhand der ID.",
        description = "Holt ein einzelnes Outfit anhand der ID, welches der eingeloggte Benutzer erstellt hat."
    )
    public TemplateInstance getOutfit(@PathParam("id") long outfitId) {
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - gestartet");
        OutfitOutputDTO outfitDTO = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitsVerwaltung.holeOutfitById(outfitId, this.sc.getPrincipal().getName()));
        outfitLog.trace(System.currentTimeMillis() + ": getOutfit-Methode - gibt ein Outfit anhand der Id fuer einen Benutzer durch Rest-Ressource");
        outfitLog.debug(System.currentTimeMillis() + ": getOutfit-Methode - beendet");
        return Templates.detail(outfitDTO);
    }
}