package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import shared.LinkDeserializer;
import shared.LinkSerializer;

public class KleidungsstueckListeOutputDTO {
    public List<KleidungsstueckOutputDTO> kleidungsstuecke;

    @JsonSerialize(using = LinkSerializer.class)
    @JsonDeserialize(using = LinkDeserializer.class)
    public List<Link> links = new ArrayList<>();

    public KleidungsstueckListeOutputDTO() {
    }


    public KleidungsstueckListeOutputDTO(List<KleidungsstueckOutputDTO> kleidungsstuecke) {
        this.kleidungsstuecke = kleidungsstuecke;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}