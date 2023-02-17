package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitTeilenDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;

@ApplicationScoped
/**
 * Realisiert die Funktionen fuer das Verwalten jeglicher Outfits.
 * @author Manuel Arling
 */
public class OutfitsVerwaltung implements OutfitProvider {
    
    @Inject
    private OutfitKatalog outfitKatalog;

    @Override
    public List<Outfit> holeAlleOutfits(OutfitFilter filter,String benutzername) {
        return this.outfitKatalog.gebeAlleOutfitsVomBenutzer(filter ,benutzername);
    }

    @Override
    public Outfit holeOutfitById(long outfitId, String benutzername) {
        return this.outfitKatalog.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
    }

    @Override
    public long erstelleOutfit(OutfitInputDTO outfitInputDTO, String benutzername) {
        return this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(outfitInputDTO, benutzername);
    }

    @Override
    public boolean loescheOutfit(long outfitId, String benutzername) {
        return this.outfitKatalog.loescheOutfitEinesBenutzers(outfitId, benutzername);
    }

    @Override
    public boolean loescheAlleOutfits(String benutzername) {
        return this.outfitKatalog.loescheAlleOutfitsEinesBenutzers(benutzername);
    }

    @Override
    public boolean bearbeiteOutfit(long outfitId, OutfitInputDTO outfitInputDTO, String benutzername) {
        return this.outfitKatalog.bearbeiteOutfitEinesBenutzers(outfitInputDTO, outfitId, benutzername);
    }

    @Override
    public boolean teileOutfit(long outfitId, OutfitTeilenDTO dto, String benutzername){
        return this.outfitKatalog.teileOutfitEinesBenutzers(outfitId, dto, benutzername);   
    }

    @Override
    public boolean entferneKleidungsstueckVonOutfit(long kleidungsId, long outfitId, String benutzername){
        return this.outfitKatalog.entferneKleidungsstueckVomOutfit(kleidungsId, outfitId, benutzername);
    }
    
    @Override
    public Outfit holeGeteiltesOutfit(long outfitId) {
        return this.outfitKatalog.gebeGeteilitesOutfit(outfitId);
    }
}


