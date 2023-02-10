package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import io.quarkus.qute.TemplateExtension;
import java.util.EnumSet;
import java.util.Set;

@TemplateExtension(namespace="FarbList")   
public final class FarbExtensions {

   public static Set<Farbe> farben() {
      return EnumSet.allOf(Farbe.class);
   } 
}