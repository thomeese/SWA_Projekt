package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Link;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckOutputDTO {

    public long kleidungsId;
    public String groesse;
    public Farbe farbe;
    public Typ typ;
    public String name;
    public List<String> kategorien;
    public Map<String, Link> links = new HashMap<>();

    public KleidungsstueckOutputDTO() {
    }


    public KleidungsstueckOutputDTO(long kleidungsId, String groesse, Farbe farbe, Typ typ, String name, List<String> kategorien) {
        this.kleidungsId = kleidungsId;
        this.groesse = groesse;
        this.farbe = farbe;
        this.typ = typ;
        this.name = name;
        this.kategorien = kategorien;
    }

    public void addLink(String name, Link link) {
        this.links.put(name, link);
    }

    
    @Override
    /**
     * @author Thomas Meese
     */
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        KleidungsstueckOutputDTO other = (KleidungsstueckOutputDTO) obj;

        if(this.kleidungsId != other.kleidungsId){
            return false;
        }

        if (!this.groesse.equals(other.groesse)) {
            return false;
        }

        if (other.farbe != this.farbe) {
            return false;
        }

        if (other.typ != this.typ) {
            return false;
        }

        if (!this.name.equals(other.name)) {
            return false;
        }

        if(this.kategorien.size() != other.kategorien.size()){
            return false;
        }

        for (int index = 0; index < this.kategorien.size(); index++) {
            if (!this.kategorien.get(index).equals(other.kategorien.get(index))) {
                return false;
            }
        }
        return true;
    }
}
