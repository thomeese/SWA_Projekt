package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository;

import java.util.List;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;

public class OutfitRepository implements OutfitKatalog{

    @Override
    public long erstelleOutfitFuerEinenBenutzer(OutfitInputDTO dto, String benutzername) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Outfit> gebeAlleOutfitsVomBenutzer(String benutzername) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Outfit gebeOutVomBenutzerMitId(long outfitId, String benutzername) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Outfit> gebeAlleVomBenutzerEinerKategorie(String kategorie, String benutzername) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Outfit gebeGeteilitesOutfit(long outfitId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
