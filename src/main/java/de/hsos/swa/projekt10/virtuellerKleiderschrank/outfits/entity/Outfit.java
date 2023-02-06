package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Vetoed
public class Outfit {
    @Id
    @GeneratedValue
    private long outfitId;
    private String name;
    private List<String> kategorien;
    private boolean istGeteilt;
    private List<Long> kleidungstuecke;
    private String benutzername;

    public Outfit(String name, List<String> kategorien, boolean istGeteilt, String benutzername){
        this.name = name;
        this.kategorien = kategorien;
        this.istGeteilt = istGeteilt;
        this.benutzername = benutzername;
        this.kleidungstuecke = new ArrayList<Long>();
    }

    public long getOutfitId() {
        return outfitId;
    }
    public void setOutfitId(long outfitId) {
        this.outfitId = outfitId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getKategorien() {
        return kategorien;
    }
    public void setKategorien(List<String> kategorien) {
        this.kategorien = kategorien;
    }
    public boolean isIstGeteilt() {
        return istGeteilt;
    }
    public void setIstGeteilt(boolean istGeteilt) {
        this.istGeteilt = istGeteilt;
    }
    public List<Long> getKleidungstuecke() {
        return kleidungstuecke;
    }
    public void setKleidungstuecke(List<Long> kleidungstuecke) {
        this.kleidungstuecke = kleidungstuecke;
    }
    public String getBenutzername() {
        return benutzername;
    }
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    
}
