package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Vetoed
public class Kleidungstueck {
    @Id
    @GeneratedValue
    private long kleidungsId;
    private String groesse;
    private Farbe farbe;
    private Typ typ;
    private String name;
    private List<String> kategorien;
    private String benutzername;

    public Kleidungstueck(String groesse, Farbe farbe, Typ typ, String name, List<String> kategorien,String benutzername){
        this.groesse = groesse;
        this.farbe = farbe;
        this.typ = typ;
        this.name = name;
        this.kategorien = kategorien;
        this.benutzername = benutzername;
    }

    public long getKleidungsId() {
        return kleidungsId;
    }
    public void setKleidungsId(long kleidungsId) {
        this.kleidungsId = kleidungsId;
    }
    public String getGroesse() {
        return groesse;
    }
    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }
    public Farbe getFarbe() {
        return farbe;
    }
    public void setFarbe(Farbe farbe) {
        this.farbe = farbe;
    }
    public Typ getTyp() {
        return typ;
    }
    public void setTyp(Typ typ) {
        this.typ = typ;
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
    public String getBenutzername() {
        return benutzername;
    }
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public boolean besitztKleidungsstueckKategorie(String kategorie){
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
