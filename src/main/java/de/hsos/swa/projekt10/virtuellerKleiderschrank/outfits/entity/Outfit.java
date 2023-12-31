package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Vetoed
@Access(AccessType.FIELD)
public class Outfit {
    @Id
    @GeneratedValue
    private long outfitId;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> kategorien = new ArrayList<>();
    private boolean geteilt;
    @ElementCollection()
    private Set<Long> kleidungsstuecke = new HashSet<>();
    private String benutzername;

    public Outfit() {
    }

    public Outfit(String name, List<String> kategorien, boolean geteilt, String benutzername){
        this.name = name;
        this.kategorien = kategorien;
        this.geteilt = geteilt;
        this.benutzername = benutzername;
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
    public boolean isGeteilt() {
        return geteilt;
    }
    public void setGeteilt(boolean geteilt) {
        this.geteilt = geteilt;
    }
    public Set<Long> getKleidungsstuecke() {
        return kleidungsstuecke;
    }
    public void setKleidungsstuecke(Set<Long> kleidungsstuecke) {
        this.kleidungsstuecke = kleidungsstuecke;
    }
    public String getBenutzername() {
        return benutzername;
    }
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public boolean besitztOutfitKategorie(String kategorie){
        if(this.kategorien.size() == 0){
            return false;
        }
        for(int index = 0; index < this.kategorien.size(); index++){
            if(this.kategorien.get(index).equals(kategorie)){
                return true;
            }
        }
        return false;
    }
    
}
