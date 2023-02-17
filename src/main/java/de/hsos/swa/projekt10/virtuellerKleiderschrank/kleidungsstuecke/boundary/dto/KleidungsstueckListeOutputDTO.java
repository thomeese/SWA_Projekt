package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Link;

public class KleidungsstueckListeOutputDTO {
    public List<KleidungsstueckOutputDTO> kleidungsstuecke;
    public Map<String, Link> links = new HashMap<>();

    public KleidungsstueckListeOutputDTO() {
    }


    public KleidungsstueckListeOutputDTO(List<KleidungsstueckOutputDTO> kleidungsstuecke) {
        this.kleidungsstuecke = kleidungsstuecke;
    }

    public void addLink(String name, Link link) {
        this.links.put(name, link);
    }
}