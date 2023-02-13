package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.HMProductDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class EnumTypMapper {
    public static Typ gibTypVonHmProduct(HMProductDTO product) {
        String productTypeNameKonvertiert = konvertiereString(product.productTypeName);
        String presentationTypeKonvertiert = konvertiereString(product.presentationTypes);
        for(Typ typ : Typ.values()) {
            String typKonvertiert = konvertiereString(typ.toString());
            if(productTypeNameKonvertiert.contains(typKonvertiert) || presentationTypeKonvertiert.contains(typKonvertiert)) {
                return typ;
            }
        }
        return Typ.Unbekannt;
    }

        private static String konvertiereString(String wort) {
        String wortKonvertiert = wort.toLowerCase();
        wortKonvertiert = wortKonvertiert.replace("ß", "ss");
        wortKonvertiert = wortKonvertiert.replace("ä", "ae");
        wortKonvertiert = wortKonvertiert.replace("ö", "oe");
        wortKonvertiert = wortKonvertiert.replace("ü", "ue");
        wortKonvertiert = wortKonvertiert.replace("-", "");
        return wortKonvertiert;
    }
}
