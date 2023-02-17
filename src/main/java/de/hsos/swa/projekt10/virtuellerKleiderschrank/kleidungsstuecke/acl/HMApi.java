package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.exceptions.ExterneAPIException;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.HMColorDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.HMProductDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.KleidungsstueckHMDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import io.quarkus.arc.log.LoggerName;

@ApplicationScoped
public class HMApi implements KleidungsstueckAPIProvider{

    @LoggerName("hm-api-rest-client")
    private static Logger hmLog = Logger.getLogger(KleidungsstueckAPIProvider.class);

    @Inject
    @RestClient
    private HMApiGateway hmApiGateway;

    @Inject
    private EnumMapper enumMapper;


    @Override
    public KleidungsstueckInputDTO holeKleidungsstueckByArtikelnummer(String artikelnummer, String groesse) throws ExterneAPIException{
        hmLog.debug(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode fuer die Artikelnummer "
            + artikelnummer + " und Groesse: " + groesse + " - gestartet");
        KleidungsstueckHMDTO kleidungsstueckHMDTO;
        try{
            kleidungsstueckHMDTO = hmApiGateway.getKleidungsstueckByArtikelnummer(artikelnummer);
            hmLog.trace(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode Hole Kleidungsstück von "
            + "H und M anhand der Artikelnummer " + artikelnummer + " durch Rest-Client");
        }catch(Exception ex) {
            hmLog.error("Beim Holen des Kleidungsstuecks von H und M ist ein Fehler aufgetreten: " + ex.getMessage());
            throw new ExterneAPIException("Beim Holen des Kleidungsstuecks von H und M ist ein Fehler aufgetreten.", ExterneAPIException.ERROR);
        }
        if(kleidungsstueckHMDTO == null || kleidungsstueckHMDTO.product == null || !kleidungsstueckHMDTO.responseStatusCode.equals("ok")) {
            throw new ExterneAPIException("Das Kleidungsstueck mit der Artikelnummer " + artikelnummer + " konnte nicht gefunden werden", ExterneAPIException.NOTFOUND);
        }
        KleidungsstueckInputDTO kleidungsstueck = new KleidungsstueckInputDTO(
            groesse, 
            this.gibGemappteFarbe(kleidungsstueckHMDTO.product.color), 
            this.gibtGemapptenTyp(kleidungsstueckHMDTO.product),
            kleidungsstueckHMDTO.product.name,
            new ArrayList<>()
        );

        hmLog.debug(System.currentTimeMillis() + ": holeKleidungsstueckByArtikelnummer-Methode fuer die Artikelnummer "
        + artikelnummer + " und Groesse: " + groesse + " - beendet");
        
        return kleidungsstueck;
    }


    private Farbe gibGemappteFarbe(HMColorDTO hmColorDTO) {
        Farbe gefundeneFarbe = enumMapper.gibNaehsteFarbeByNamenvergleich(hmColorDTO.text);
        if(gefundeneFarbe != Farbe.Unbekannt) {
            return gefundeneFarbe;
        }
        return enumMapper.gibNaesteFarbeByRGB(hmColorDTO.rgbColor);
    }

    private Typ gibtGemapptenTyp(HMProductDTO hmProductDTO) {
        Typ gefundenerTyp = enumMapper.gibZugehoerigenTypByNamenvergleich(hmProductDTO.productTypeName);
        if(gefundenerTyp == null || gefundenerTyp == Typ.Unbekannt) {
            return enumMapper.gibZugehoerigenTypByNamenvergleich(hmProductDTO.presentationTypes);
        }
        return gefundenerTyp;
    }
}