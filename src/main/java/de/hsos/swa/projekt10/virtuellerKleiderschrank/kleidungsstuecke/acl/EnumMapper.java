package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import java.awt.Color;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.FarbExtensions;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;

public class EnumMapper {
    public static Farbe gibNaehsteFarbe(String hexCode) {
        Color inputFarbe = Color.decode(hexCode);
        double geringsterFarbAbstand = Double.MAX_VALUE;
        Farbe naechstmoeglicheFarbe = null;
        for(Farbe farbe : Farbe.values()) {
            if(farbe.farbwert != null) {
                double deltaE = farbabstandDeltaE(inputFarbe, farbe.farbwert);
                if(deltaE < geringsterFarbAbstand) {
                    geringsterFarbAbstand = deltaE;
                    naechstmoeglicheFarbe = farbe;
                }
            }
        }
        return naechstmoeglicheFarbe;
    }
    
    private static double farbabstandDeltaE(Color farbe1, Color farbe2) {
        return Math.sqrt(Math.pow((farbe2.getRed()-farbe1.getRed()),2) 
            + Math.pow((farbe2.getGreen()-farbe1.getGreen()), 2) 
            + Math.pow((farbe2.getBlue()-farbe1.getBlue()), 2));
    }
}
