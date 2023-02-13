package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;

public interface KleidungsstueckAPIProvider {
    public KleidungsstueckInputDTO holeKleidungsstueckByArtikelnummer(String artikelnummer, String groesse);
}
