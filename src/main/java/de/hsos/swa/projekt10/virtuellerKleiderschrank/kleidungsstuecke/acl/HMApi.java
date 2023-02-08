package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.KleidungsstueckHMDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;

@ApplicationScoped
public class HMApi implements KleidungsstueckAPIProvider{
   
    @Inject
    @RestClient
    private HMApiGateway hmApiGateway;

    @Override
    public KleidungsstueckInputDTO holeKleidungsstueckByArtikelnummer(long artikelnummer, String groesse) {
        KleidungsstueckHMDTO.Converter.toKleidungsstueckInputDTO(hmApiGateway.getKleidungsstueckByArtikelnummer(artikelnummer), groesse);
        return null;
    }

}