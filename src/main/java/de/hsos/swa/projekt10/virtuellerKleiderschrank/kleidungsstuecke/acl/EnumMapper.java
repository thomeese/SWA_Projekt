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

    public Farbe gibNaesteFarbeByRGB(String hexCode) {
       double[] ergebnisCIEXYZ = konvertiereZuCIEXYZ(Color.decode("#98A68F"));
        double[] ergebnisCIELab = konvertiereZuCIELab(Color.decode("#98A68F"));
        double abstand = farbabstandCIE94(Color.decode("#FF7A7A"), Color.decode("#98A68F"));

        Color inputFarbe = Color.decode(hexCode);
        double geringsterFarbAbstand = Double.MAX_VALUE;
        Farbe naechstmoeglicheFarbe = null;
        for(Farbe farbe : Farbe.values()) {
            if(farbe.equals(Farbe.Grau) || farbe.equals(Farbe.Hellgrau) || farbe.equals(Farbe.Grau) || farbe.equals(Farbe.Schwarz)) {
                if(inputFarbe.getBlue() != inputFarbe.getGreen() || inputFarbe.getGreen() != inputFarbe.getRed()) {
                    //alle drei RGB-Werte haben nicht den gleichen Wert
                    //dadurch kann inputFarbe keins von den Farben weiss, grau, schwarz
                    continue;
                }
            }
            if(farbe.farbwert != null && farbe != Farbe.Beige) {
                double deltaE = farbabstandCIE94(inputFarbe, farbe.farbwert);
                 if(deltaE < geringsterFarbAbstand) {
                    geringsterFarbAbstand = deltaE;
                    naechstmoeglicheFarbe = farbe;
                }
            }
        }
        return naechstmoeglicheFarbe;
    }

    private double farbabstandCIE94(Color farbe1, Color farbe2) {
        double[] lab1 = konvertiereZuCIELab(farbe1);
        double[] lab2 = konvertiereZuCIELab(farbe2);
        
        double kc = 1.0; // 1 default
        double kh = 1.0; // 1 default
        double kl = 1.0; // 1 default, 2 textiles
        double k1 = 0.045; // 0.045 graphic arts, 0.048 textiles
        double k2 = 0.015; // 0.015 graphic arts, 0.014 textiles
        double sl = 1.0;


        double deltaL = lab1[0]-lab2[0];
        double c1 = Math.sqrt((lab1[1] * lab1[1] + lab1[2] * lab1[2]));
        double c2 = Math.sqrt((lab2[1] * lab2[1] + lab2[2] * lab2[2]));
        double deltaC = c1 - c2;
        double deltaA = lab1[1]-lab2[1];
        double deltaB = lab1[2]-lab2[2];
        double deltaH = deltaA*deltaA + deltaB*deltaB - deltaC*deltaC;
        deltaH = (deltaH < 0) ? 0.0 : Math.sqrt(deltaH);
        double sc = 1.0 + k1*c1;
        double sh = 1.0 + k2*c1;

        double e94 = Math.pow(deltaL/ (kl*sl), 2) + Math.pow(deltaC/ (kc*sc), 2) + Math.pow(deltaH / (kh*sh), 2);
        e94 = (e94 < 0) ? 0 : Math.sqrt(e94);

        return e94;
    }

    private double[] konvertiereZuCIEXYZ(Color farbe) {
        double r = farbe.getRed() / 255.0;
        double g = farbe.getGreen() / 255.0;
        double b = farbe.getBlue() / 255.0;

        double rs = farbe.getRed();
        double gs = farbe.getGreen();
        double bs = farbe.getBlue();


        //liear values/gamma-expanded
        r = (r > 0.04045) ? Math.pow(((r+0.055)/1.055), 2.4) : r/12.92;
        g = (g > 0.04045) ? Math.pow(((g+0.055)/1.055), 2.4) : g/12.92;
        b = (b > 0.04045) ? Math.pow(((b+0.055)/1.055), 2.4) : b/12.92;

        //Matric um CIE XYZ zu bekommen
        double[][] matrix = {   {0.4124, 0.3576, 0.1805},
                                {0.2126, 0.7152, 0.0722},
                                {0.0193, 0.1192, 0.9505}    };

        double x = r * matrix[0][0] + g * matrix[0][1] + b * matrix[0][2];
        double y = r * matrix[1][0] + g * matrix[1][1] + b * matrix[1][2];
        double z = r * matrix[2][0] + g * matrix[2][1] + b * matrix[2][2];

        double[] xyz = {x,y,z};
        return xyz;
    }

    private double[] konvertiereZuCIELab(Color farbe) {
        double[] farbeCIEXYZ = konvertiereZuCIEXYZ(farbe);
        farbeCIEXYZ[0] *= 100;
        farbeCIEXYZ[1] *= 100;
        farbeCIEXYZ[2] *= 100;

        //sRGB standard light CIE1931
        double Xn = 95.0489;
        double Yn = 100;
        double Zn = 108.8840;

        double fx = f(farbeCIEXYZ[0]/Xn);
        double fy = f(farbeCIEXYZ[1]/Yn);
        double fz = f(farbeCIEXYZ[2]/Zn);

        double l = 116 * fy - 16.0;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);
        
        double[] lab = {l, a, b};
        return lab;
    }

    private double f(double t) {
        double delta = 6.0/29.0;
        if( t > Math.pow(delta, 3)) {
            return Math.pow(t, 1.0/3.0); 
        } else {
            return t/3*Math.pow(delta, 2) + 4.0/29.0;
        }
    }
}
