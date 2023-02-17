package shared;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckListeOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest.KleidungsstueckIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest.KleidungsstueckeRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitListOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitIdRestRessource;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest.OutfitRestRessource;

@ApplicationScoped
public class LinkBuilder {


    public void addLinksZuKleidungsstueckOutputDTO(KleidungsstueckOutputDTO kleidungsstueckOutputDTO, UriInfo uriInfo) {
        addLinkZuKleidungsstueckOutputDTO("self", "get kleidungsstueck", "GET", kleidungsstueckOutputDTO, uriInfo);
        addLinkZuKleidungsstueckOutputDTO("edit", "edit kleidungsstueck", "PATCH", kleidungsstueckOutputDTO, uriInfo);
        addLinkZuKleidungsstueckOutputDTO("delete", "delete kleidungsstueck", "DELETE", kleidungsstueckOutputDTO, uriInfo);
        addLinkKategorieZuKleidungsstueckOutputDTO("add-category", "add kategorie zu kleidungsstueck", "POST", kleidungsstueckOutputDTO, uriInfo, "fuegeKategorieHinzu");
        addLinkKategorieZuKleidungsstueckOutputDTO("remove-category", "remove kategorie von kleidungsstueck", "DELETE", kleidungsstueckOutputDTO, uriInfo, "fuegeKategorieHinzu");
        addLinkCollectionZuKleidungsstueckOutputDTO("collection", "get alle kleidungsstuecke", "GET", kleidungsstueckOutputDTO, uriInfo);
    }

    private void addLinkCollectionZuKleidungsstueckOutputDTO(String rel, String titel, String httpMethode, KleidungsstueckOutputDTO kleidungsstueckOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(KleidungsstueckeRestRessource.class, uriInfo);
            Link link = Link.fromUri(selfUri)
                            .rel(rel)
                            .type(MediaType.APPLICATION_JSON)
                            .title(titel)
                            .param("method", httpMethode)
                            .build();
                            kleidungsstueckOutputDTO.addLink(link);
        }

    private void addLinkKategorieZuKleidungsstueckOutputDTO(String rel, String titel, String httpMethode, KleidungsstueckOutputDTO kleidungsstueckOutputDTO, UriInfo uriInfo, String ressourceMethodennamen) {
    URI selfUri = erstelleRessourceUri(KleidungsstueckIdRestRessource.class, kleidungsstueckOutputDTO.kleidungsId, ressourceMethodennamen, uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel(rel)
                        .type(MediaType.APPLICATION_JSON)
                        .title(titel)
                        .param("method", httpMethode)
                        .build();
                        kleidungsstueckOutputDTO.addLink(link);
    }

    private void addLinkZuKleidungsstueckOutputDTO(String rel, String titel, String httpMethode, KleidungsstueckOutputDTO kleidungsstueckOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(KleidungsstueckIdRestRessource.class, kleidungsstueckOutputDTO.kleidungsId, uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel(rel)
                        .type(MediaType.APPLICATION_JSON)
                        .title(titel)
                        .param("method", httpMethode)
                        .build();
                        kleidungsstueckOutputDTO.addLink(link);
    }

    public void addLinksZuKleidungsstueckeListeOutputDTO(KleidungsstueckListeOutputDTO kleidungsstueckListeOutputDTO, UriInfo uriInfo) {
        this.addLinkZuKleidungsstueckeListeOutputDTO("self", "get alle Kleidungsstuecke", "GET", kleidungsstueckListeOutputDTO, uriInfo);
        this.addLinkZuKleidungsstueckeListeOutputDTO("delete", "delete alle Kleidungsstuecke", "DELETE", kleidungsstueckListeOutputDTO, uriInfo);
    }

    private void addLinkZuKleidungsstueckeListeOutputDTO(String rel, String titel, String httpMethode, KleidungsstueckListeOutputDTO kleidungsstueckListeOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(KleidungsstueckeRestRessource.class, uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel(rel)
                        .type(MediaType.APPLICATION_JSON)
                        .title(titel)
                        .param("method", httpMethode)
                        .build();
                        kleidungsstueckListeOutputDTO.addLink(link);
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

    // Links fuer Outfits
    public void addLinksZuOutfitOutputDTO(OutfitOutputDTO outfitOutputDTO, UriInfo uriInfo) {
        addLinkZuOutfitOutputDTO("self", "get outfit", "GET", outfitOutputDTO, uriInfo);
        addLinkZuOutfitOutputDTO("edit", "edit outfit", "PATCH", outfitOutputDTO, uriInfo);
        addLinkZuOutfitOutputDTO("delete", "delete outfit", "DELETE", outfitOutputDTO, uriInfo);
        addLinkCollectionZuOutfitOutputDTO("collection", "get alle outfits", "GET", outfitOutputDTO, uriInfo);
    }

    private void addLinkCollectionZuOutfitOutputDTO(String rel, String titel, String httpMethode, OutfitOutputDTO outfitOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(OutfitRestRessource.class, uriInfo);
            Link link = Link.fromUri(selfUri)
                            .rel(rel)
                            .type(MediaType.APPLICATION_JSON)
                            .title(titel)
                            .param("method", httpMethode)
                            .build();
                            outfitOutputDTO.addLink(link);
        }

    private void addLinkZuOutfitOutputDTO(String rel, String titel, String httpMethode, OutfitOutputDTO outfitOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(OutfitIdRestRessource.class,outfitOutputDTO.outfitId, uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel(rel)
                        .type(MediaType.APPLICATION_JSON)
                        .title(titel)
                        .param("method", httpMethode)
                        .build();
                        outfitOutputDTO.addLink(link);
    }

    public void addLinksZuOutfitListeOutputDTO(OutfitListOutputDTO outfitListOutputDTO, UriInfo uriInfo) {
        this.addLinkZuOutfitListeOutputDTO("self", "get alle outfits", "GET", outfitListOutputDTO, uriInfo);
        this.addLinkZuOutfitListeOutputDTO("delete", "delete alle outfits", "DELETE", outfitListOutputDTO, uriInfo);
    }

    private void addLinkZuOutfitListeOutputDTO(String rel, String titel, String httpMethode, OutfitListOutputDTO outfitListOutputDTO, UriInfo uriInfo) {
        URI selfUri = erstelleRessourceUri(OutfitRestRessource.class, uriInfo);
        Link link = Link.fromUri(selfUri)
                        .rel(rel)
                        .type(MediaType.APPLICATION_JSON)
                        .title(titel)
                        .param("method", httpMethode)
                        .build();
                        outfitListOutputDTO.addLink(link);
    }
}
