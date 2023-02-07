package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control;

import java.util.List;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;

public class OutfitsVerwaltung {
    
    @Inject
    private OutfitKatalog outfitKatalog;

    public List<Outfit> holeAlleOutfits(String benutzername) {
        return this.outfitKatalog.gebeAlleOutfitsVomBenutzer(benutzername);
    }

    public Outfit holeOutfitById(long outfitId, String benutzername) {
        return this.outfitKatalog.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
    }

    public long erstelleOutfit(OutfitInputDTO outfitInputDTO, String benutzername) {
        return this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(outfitInputDTO, benutzername);
    }

    public boolean loescheOutfit(long outfitId, String benutzername) {
        return this.outfitKatalog.loescheOutfitEinesBenutzers(outfitId, benutzername);
    }

    public boolean loescheAlleOutfits(String benutzername) {
        return this.outfitKatalog.loescheAlleOutfitsEinesBenutzers(benutzername);
    }

    public boolean bearbeiteOutfit(long outfitId, OutfitInputDTO outfitInputDTO, String benutzername) {
        return this.outfitKatalog.bearbeiteOutfitEinesBenutzers(outfitInputDTO, outfitId, benutzername);
    }
}


