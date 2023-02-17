package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.control;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitTeilenDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;

public interface OutfitProvider {
    public List<Outfit> holeAlleOutfits(OutfitFilter filter,String benutzername);

    public Outfit holeOutfitById(long outfitId, String benutzername);

    public long erstelleOutfit(OutfitInputDTO outfitInputDTO, String benutzername);

    public boolean loescheOutfit(long outfitId, String benutzername);

    public boolean loescheAlleOutfits(String benutzername);

    public boolean bearbeiteOutfit(long outfitId, OutfitInputDTO outfitInputDTO, String benutzername);

    public boolean teileOutfit(long outfitId, OutfitTeilenDTO dto, String benutzername);

    public boolean entferneKleidungsstueckVonOutfit(long kleidungsId, long outfitId, String benutzername);
    
    public Outfit holeGeteiltesOutfit(long outfitId);
}
