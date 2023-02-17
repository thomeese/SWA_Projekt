package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;


import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;


import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.KeycloakTestTokenService;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository.KleidungsstueckRepository;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository.OutfitRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.Assert;

@QuarkusTest
@TestHTTPEndpoint(OutfitIdRestRessource.class)
public class OutfitIdRestRessourceTest {
    @Inject
    KeycloakTestTokenService service;

    @Inject
    KleidungsstueckRepository kleidungsstueckKatalog;

    @Inject
    OutfitRepository outfitKatalog;

    @Inject
    DTOKonverter kDtoKonverter;

    @Inject
    ObjectMapper objectMapper;

    private OutfitOutputDTO testOutfit;

    private static final String benutzername = "testbenutzer";
    private static final String passwort = "test";

    @BeforeEach
    public void init() {
        // Füge Beispiel-Daten in die Datenbank ein
        KleidungsstueckInputDTO k1 = new KleidungsstueckInputDTO("M", Farbe.Blau, Typ.Hemd, "business",
                Arrays.asList("Herren"));
        KleidungsstueckInputDTO k2 = new KleidungsstueckInputDTO("S", Farbe.Rot, Typ.TShirt, "casual",
                Arrays.asList("Damen"));
        KleidungsstueckInputDTO k3 = new KleidungsstueckInputDTO("L", Farbe.Gruen, Typ.Pullover, "casual",
                Arrays.asList("Herren", "Damen"));

        this.kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k1, benutzername);
        this.kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k2, benutzername);
        this.kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k3, benutzername);
        List<Kleidungsstueck> kleidungsstuecke = kleidungsstueckKatalog
                .gebeAlleKleidungsstueckeVomBenutzer(new KleidungsstueckFilter(), benutzername);
        OutfitInputDTO o1 = new OutfitInputDTO("Tolles Outfit", Arrays.asList("Sommer"),
                Arrays.asList(kleidungsstuecke.get(1).getKleidungsId(), kleidungsstuecke.get(2).getKleidungsId()),
                false);
        this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(o1, benutzername);
        // Lese die Beispiel-Daten aus der DatenbankS
        this.testOutfit = OutfitOutputDTO.Converter.toOutfitOutputDTO(this.outfitKatalog.gebeAlleOutfitsVomBenutzer(new OutfitFilter(), benutzername).get(0));

    }

    @AfterEach
    public void cleanUp() {
        this.kleidungsstueckKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzername);
        this.outfitKatalog.loescheAlleOutfitsEinesBenutzers(benutzername);
    }

    @Test
    void testBearbeiteOutfit() {
        String name = "Neuer Name";
        OutfitInputDTO dto = new OutfitInputDTO(name, new ArrayList<String>(), new ArrayList<Long>(), false);
        
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .body(dto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .pathParam("id", this.testOutfit.outfitId)
                .patch();

        String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testOutfit.outfitId)
                .get()
                .then()
                .extract()
                .body().asString();

        OutfitOutputDTO erhaltenesOutfit = null;

        try {
            erhaltenesOutfit = objectMapper.readValue(response, OutfitOutputDTO.class);
            Assert.assertTrue(erhaltenesOutfit.name.equals(name));
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }
    }

    @Test
    void testEntferneKleidungsstueck() {
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testOutfit.outfitId)
                .pathParam("kleidungsId", this.testOutfit.kleidungsstuecke.get(0))
                .delete("/{kleidungsId}");

                String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testOutfit.outfitId)
                .get()
                .then()
                .extract()
                .body().asString();

        OutfitOutputDTO erhaltenesOutfit = null;
        try {
            erhaltenesOutfit = objectMapper.readValue(response, OutfitOutputDTO.class);
            for (Long id : erhaltenesOutfit.kleidungsstuecke) {
                if (id == testOutfit.kleidungsstuecke.get(0)) {
                    Assert.assertTrue(false);
                }
            }
            Assert.assertTrue(true);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

    }

    @Test
    void testGetOutfit() {
        String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testOutfit.outfitId)
                .get()
                .then()
                .extract()
                .body().asString();

        OutfitOutputDTO erhaltenesOutfit = null;

        try {
            erhaltenesOutfit = objectMapper.readValue(response, OutfitOutputDTO.class);
            Assert.assertTrue(erhaltenesOutfit.equals(testOutfit));
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }
    }

    @Test
    void testLoescheOutfit() {
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testOutfit.outfitId)
                .delete();

        Outfit outfit = this.outfitKatalog.gebeOutfitVomBenutzerMitId(this.testOutfit.outfitId, benutzername);
        Assert.assertTrue(outfit == null);
    }

    @Test
    void testTeileOutfit() {

    }
}
