package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;

public class OutiftsVerwaltung {
    
    @Inject
    private OutfitKatalog outfitKatalog;

    public List<Outfit> holeAlleOutfits(String benutzername) {
        return this.outfitKatalog.gebeAlleOutfitsVomBenutzer(benutzername);
    }

    public Outfit holeOutfitById(long outfitId, String benutzername) {
        return this.outfitKatalog.gebeOutfitVomBenutzerMitId(outfitId, benutzername);
    }

    public boolean erstelleOutfit(OutfitInputDTO outfitInputDTO, String benutzername) {
        return this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(outfitInputDTO, benutzername);
    }
}


