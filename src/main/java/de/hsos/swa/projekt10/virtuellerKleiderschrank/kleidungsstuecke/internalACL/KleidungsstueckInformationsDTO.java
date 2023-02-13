package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL;

public class KleidungsstueckInformationsDTO {
    public String name;
    public long kleidungsId;

    public KleidungsstueckInformationsDTO(){

    }

    public KleidungsstueckInformationsDTO(String name, long id){
        this.name = name;
        this.kleidungsId = id;
    }
}
