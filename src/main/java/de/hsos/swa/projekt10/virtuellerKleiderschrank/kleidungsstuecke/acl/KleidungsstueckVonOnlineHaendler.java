package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;

@ApplicationScoped
public class KleidungsstueckVonOnlineHaendler {

    @Inject
    private HMApi hmApi;

    public KleidungsstueckInputDTO holeKleidungsstueckVonHM(String artikelnummer, String groesse) throws ExterneAPIException {
        return hmApi.holeKleidungsstueckByArtikelnummer(artikelnummer, groesse);
    }
}
