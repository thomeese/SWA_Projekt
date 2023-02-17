package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import java.awt.Color;

import javax.enterprise.context.Dependent;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

@Dependent
public class EnumMapper {
    public Typ gibZugehoerigenTypByNamenvergleich(String typNamen) {
        String typNamenKonvertiert = konvertiereString(typNamen);
        for(Typ typ : Typ.values()) {
            String typKonvertiert = konvertiereString(typ.toString());
            if(typNamenKonvertiert.contains(typKonvertiert) || typNamenKonvertiert.contains(typKonvertiert)) {
                return typ;
            }
        }
        return Typ.Unbekannt;
    }

    public Farbe gibNaehsteFarbeByNamenvergleich(String name) {
        for(Farbe farbe: Farbe.values()) {
            if (namensvergleich(farbe.toString(), name)) {
                return farbe;
            }
        }
        return Farbe.Unbekannt;
    }

    private boolean namensvergleich(String farbname1, String farbname2) {
        String farbname1Konvertiert = konvertiereString(farbname1);
        String farbname2Konvertiert = konvertiereString(farbname2);

        return farbname1Konvertiert.contains(farbname2Konvertiert) || farbname2Konvertiert.contains(farbname1Konvertiert);
    }

        private String konvertiereString(String wort) {
        String wortKonvertiert = wort.toLowerCase();
        wortKonvertiert = wortKonvertiert.replace("ß", "ss");
        wortKonvertiert = wortKonvertiert.replace("ä", "ae");
        wortKonvertiert = wortKonvertiert.replace("ö", "oe");
        wortKonvertiert = wortKonvertiert.replace("ü", "ue");
        wortKonvertiert = wortKonvertiert.replace("-", "");
        return wortKonvertiert;
    }
}
