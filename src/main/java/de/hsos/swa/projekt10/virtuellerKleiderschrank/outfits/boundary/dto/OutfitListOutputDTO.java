package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import shared.LinkDeserializer;
import shared.LinkSerializer;

public class OutfitListOutputDTO {
    public List<OutfitOutputDTO> outfits;

    @JsonSerialize(using = LinkSerializer.class)
    @JsonDeserialize(using = LinkDeserializer.class)
    public List<Link> links = new ArrayList<>();

    public OutfitListOutputDTO(List<OutfitOutputDTO> outfits) {
        this.outfits = outfits;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
}
