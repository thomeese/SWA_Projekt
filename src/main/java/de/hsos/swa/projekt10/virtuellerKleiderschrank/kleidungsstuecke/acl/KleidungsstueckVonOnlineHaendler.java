package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;

public class KleidungsstueckVonOnlineHaendler {

    @Inject
    private HMApi hmApi;

    public KleidungsstueckInputDTO holeKleidungsstueckVonHM(long artikelnummer, String groesse) {
        return hmApi.holeKleidungsstueckByArtikelnummer(artikelnummer, groesse);
    }
}
