package shared;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.UriInfo;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest.KleidungsstueckIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest.KleidungsstueckeRestRessource;

@ApplicationScoped
public class RessourceUriBuilder {
    
    public URI fuerKleidungsstueck(long kleidungsId, UriInfo uriInfo) {
        return erstelleRessourceUri(KleidungsstueckIdRestRessource.class, kleidungsId, uriInfo);
    }

    public URI fuerKleidungsstuecke(UriInfo uriInfo) {
        return erstelleRessourceUri(KleidungsstueckeRestRessource.class, uriInfo);
    }
    
    public URI fuerKategorieKleidungsstueck(long kleidungsId, String methode, UriInfo uriInfo) {
        return erstelleRessourceUri(KleidungsstueckIdRestRessource.class, kleidungsId, methode, uriInfo);
    }

    private URI erstelleRessourceUri(Class<?> ressourceKlasse, long id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(ressourceKlasse).build(id);
    }

    private URI erstelleRessourceUri(Class<?> ressourceKlasse, long id, String methode, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(ressourceKlasse).path(ressourceKlasse, methode).build(id);
    }

    private URI erstelleRessourceUri(Class<?> ressourceKlasse, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(ressourceKlasse).build();
    }

}
