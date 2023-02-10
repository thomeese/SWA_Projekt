package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity;

import io.quarkus.qute.TemplateExtension;
import java.util.EnumSet;
import java.util.Set;

@TemplateExtension(namespace="TypList")   
public final class TypExtensions {

   public static Set<Typ> typen() {
      return EnumSet.allOf(Typ.class);
   } 
}