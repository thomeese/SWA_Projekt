package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto;

import java.util.ArrayList;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.EnumFarbeMapper;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.EnumTypMapper;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;

public class KleidungsstueckHMDTO {
    public String responseStatusCode;
    public HMProductDTO product;

    public static class Converter {
        public static KleidungsstueckInputDTO toKleidungsstueckInputDTO(KleidungsstueckHMDTO kleidungsstueckHMDTO, String groesse) {
            //TODO Mapper wegen Typ und Farbe benoetigt
            //return new KleidungsstueckInputDTO(kleidungsstueckHMDTO.product.color.text, kleidungsstueckHMDTO.product.productTypeName, kleidungsstueckHMDTO.product.name, new ArrayList<String>());
            
            return new KleidungsstueckInputDTO(groesse, EnumFarbeMapper.gibNaehsteFarbe(kleidungsstueckHMDTO.product.color), EnumTypMapper.gibTypVonHmProduct(kleidungsstueckHMDTO.product), kleidungsstueckHMDTO.product.name, new ArrayList<String>());
        }
    }
}
