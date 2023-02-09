package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.KleidungsstueckHMDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import io.quarkus.arc.log.LoggerName;

@ApplicationScoped
public class HMApi implements KleidungsstueckAPIProvider{

    @LoggerName("hm-api")
    private static Logger hmLog = Logger.getLogger(KleidungsstueckAPIProvider.class);

    @Inject
    @RestClient
    private HMApiGateway hmApiGateway;


    @Override
    public KleidungsstueckInputDTO holeKleidungsstueckByArtikelnummer(long artikelnummer, String groesse) {
        hmLog.debug(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode - gestartet");
        KleidungsstueckInputDTO kleidungsstueck = KleidungsstueckHMDTO.Converter.toKleidungsstueckInputDTO(hmApiGateway.getKleidungsstueckByArtikelnummer(artikelnummer), groesse);
        hmLog.trace(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode - holt Kleidungsstueck von H und M anhand der Artikelnummer durch Rest-Client");
        hmLog.debug(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode - beendet");
        
        return kleidungsstueck;
    }

}