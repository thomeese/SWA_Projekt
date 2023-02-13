package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import java.awt.Color;
import java.util.stream.Stream;

public enum Farbe {
    Beige(Color.decode("#d1bc8a")),
    Blau(Color.BLUE),
    Braun(Color.decode("#8A6642")),
    Gelb(Color.YELLOW),
    Gold(Color.decode("#D4AF37")),
    Grau(Color.GRAY),
    Gruen(Color.GREEN),
    Lila(Color.decode("#8800ff")),
    Mehrfarbig(null),
    Orange(Color.ORANGE),
    Rosa(Color.decode("#FFB6C1")),
    Rot(Color.RED),
    Schwarz(Color.BLACK),
    Silber(Color.decode("#C0C0C0")),
    Tuerkis(Color.decode("#40E0D0")),
    Weiss(Color.WHITE),
    Hellgrau (Color.LIGHT_GRAY),
    Antrazit(Color.decode("#293133"));

    public final Color farbwert;

    private Farbe(Color farbwert) {
        this.farbwert = farbwert;
    }

    public static Farbe valueOfLabel(Color farbe) {
        return Stream.of(Farbe.values()).filter(farbeIter -> farbeIter.farbwert.equals(farbe)).findFirst().orElse(null);
    }
}
