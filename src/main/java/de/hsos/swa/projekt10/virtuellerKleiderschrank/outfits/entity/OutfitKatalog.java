package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity;

import java.util.List;

public interface OutfitKatalog {
    public long erstelleOutfitFuerEinenBenutzer(OutfitInputDTO dto, String benutzername);
    public boolean bearbeiteOutfitEinesBenutzers(OutfitInputDTO dto, String benutzername);
    public boolean loescheOutfitEinesBenutzers(long outfitId, String benutzername);
    public boolean loescheAlleOutfitsEinesBenutzers(String benutzername);
    public Outfit fuegeKleidungsstueckZuOutfitHinzu(long kleidungsId, long outfitId, String benutzername);
    public boolean entferneKleidungsstueckVomOutfit(long kleidungsId, long outfitId, String benutzername);
    public List<Outfit> gebeAlleOutfitsVomBenutzer(String benutzername);
    public Outfit gebeOutVomBenutzerMitId(long outfitId, String benutzername);
    public List<Outfit> gebeAlleVomBenutzerEinerKategorie(String kategorie, String benutzername);
    public Outfit gebeGeteilitesOutfit(long outfitId);
}
