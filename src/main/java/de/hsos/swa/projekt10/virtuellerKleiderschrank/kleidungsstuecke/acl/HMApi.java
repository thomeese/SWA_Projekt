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
/**
 * Stellt den Adapter fuer den Zugriff auf die Externe API dar, um Informatioen zu Artikel zu bekommen.
 * @author Manuel Arling
 */
public class HMApi{

    @LoggerName("hm-api-rest-client")
    private static Logger hmLog = Logger.getLogger(HMApi.class);

    @Inject
    @RestClient
    private HMApiGateway hmApiGateway;

    @Inject
    private EnumMapper enumMapper;

    /**
     * Diese Methode ruft ein Kleidungsstück aus der H&M-API anhand der angegebenen Artikelnummer ab.
     *
     * @param artikelnummer Die Artikelnummer des zu suchenden Kleidungsstücks
     * @return Ein DTO-Objekt, das die Daten des abgerufenen Kleidungsstücks enthält
     * @author Manuel Arling
     */
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

    /**
     * Methode um ein HMColorDTO Objekt in eine Farbe umzuwandlen.
     * Dabei wird mithilfe des EnumMappers die passende Farbe gefunden.
     * 
     * @param hmColorDTO Objekt vom Typ HMColorDTO, das die Farbdaten enthaelt.
     * @return die gemappte Farbe
     * @author Manuel Arling
     */
    private Farbe gibGemappteFarbe(HMColorDTO hmColorDTO) {
        return enumMapper.gibNaehsteFarbeByNamenvergleich(hmColorDTO.text);
    }

    /**
     * Methode um vom HMProductDTO Objekt den Typen in ein Typ von der Enum Typ umzuwandlen.
     * Dabei wird mithilfe des EnumMappers die passende Farbe gefunden.
     * 
     * @param hmProductDTO Objekt vom Typ HMProductDTO, das Informationen zum Typ enthaelt.
     * @return gemappter Typ
     * @author Manuel Arling
     */
    private Typ gibtGemapptenTyp(HMProductDTO hmProductDTO) {
        Typ gefundenerTyp = enumMapper.gibZugehoerigenTypByNamenvergleich(hmProductDTO.productTypeName);
        if(gefundenerTyp == null || gefundenerTyp == Typ.Unbekannt) {
            return enumMapper.gibZugehoerigenTypByNamenvergleich(hmProductDTO.presentationTypes);
        }
        return gefundenerTyp;
    }
}