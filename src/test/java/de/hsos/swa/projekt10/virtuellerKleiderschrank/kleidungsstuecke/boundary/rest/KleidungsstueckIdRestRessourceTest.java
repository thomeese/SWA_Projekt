package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.KeycloakTestTokenService;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KategorieDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.KleidungsstueckOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Farbe;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Typ;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.gateway.repository.KleidungsstueckRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(KleidungsstueckIdRestRessource.class)
public class KleidungsstueckIdRestRessourceTest {
    @Inject
    KeycloakTestTokenService service;

    @Inject
    KleidungsstueckRepository kleidungsstueckKatalog;

    @Inject
    DTOKonverter kDtoKonverter;

    @Inject
    ObjectMapper objectMapper;

    private KleidungsstueckOutputDTO testKleidungsstueck;

    private static final String benutzername = "testbenutzer";
    private static final String passwort = "test";

    @BeforeEach
    public void init() {
        // Füge Beispiel-Daten in die Datenbank ein
        KleidungsstueckInputDTO k1 = new KleidungsstueckInputDTO("M", Farbe.Blau, Typ.Hemd, "business",
                Arrays.asList("Herren"));

        kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k1, benutzername);

        // Lese die Beispiel-Daten aus der Datenbank
        List<Kleidungsstueck> kleidungsstuecke = kleidungsstueckKatalog
                .gebeAlleKleidungsstueckeVomBenutzer(new KleidungsstueckFilter(), benutzername);

        testKleidungsstueck = kDtoKonverter.konvert(kleidungsstuecke.get(0));
    }

    @AfterEach
    public void cleanUp() {
        this.kleidungsstueckKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzername);
    }

    @Test
    void testBearbeiteKleidungsstueck() {
        String name = "Neuer Name";
        KleidungsstueckInputDTO k1 = new KleidungsstueckInputDTO("M", Farbe.Blau, Typ.Hemd, name,
                Arrays.asList("Herren"));
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .body(k1)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .pathParam("id", this.testKleidungsstueck.kleidungsId)
                .patch();

        Kleidungsstueck kleidung = this.kleidungsstueckKatalog
                .gebeKleidungsstueckVomBenutzerMitId(this.testKleidungsstueck.kleidungsId, benutzername);
        if (kleidung == null) {
            Assert.assertTrue(false);
        }
        Assert.assertTrue(kleidung.getName().equals(name));

    }

    @Test
    void testEntferneKategorie() {
        String kategorieName = "Herren";
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testKleidungsstueck.kleidungsId)
                .pathParam("category", kategorieName)
                .delete("/category/{category}");

        Kleidungsstueck kleidung = this.kleidungsstueckKatalog
                .gebeKleidungsstueckVomBenutzerMitId(this.testKleidungsstueck.kleidungsId, benutzername);
        if (kleidung == null) {
            Assert.assertTrue(false);
        }

        for (String kategorie : kleidung.getKategorien()) {
            if (kategorie.equals(kategorieName)) {
                Assert.assertTrue(false);
            }
        }

        Assert.assertTrue(true);

    }

    @Test
    void testFuegeKategorieHinzu() {
        KategorieDTO dto = new KategorieDTO();
        dto.kategorieNamen = "Neue Kategorie";
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .body(dto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .pathParam("id", this.testKleidungsstueck.kleidungsId)
                .post();

        Kleidungsstueck kleidung = this.kleidungsstueckKatalog
                .gebeKleidungsstueckVomBenutzerMitId(this.testKleidungsstueck.kleidungsId, benutzername);
        if (kleidung == null) {
            Assert.assertTrue(false);
        }

        for(String kategorie: kleidung.getKategorien()){
            if(kategorie.equals(dto.kategorieNamen)){
                Assert.assertTrue(true);
            }
        }
        Assert.assertTrue(false);

    }

    @Test
    void testGetKleidungsstueck() {
        String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testKleidungsstueck.kleidungsId)
                .get()
                .then()
                .extract()
                .body().asString();

        KleidungsstueckOutputDTO erhalteneKleidungsstuecke = null;

        try {
            erhalteneKleidungsstuecke = objectMapper.readValue(response, KleidungsstueckOutputDTO.class);
            Assert.assertTrue(erhalteneKleidungsstuecke.equals(testKleidungsstueck));
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

    }

    @Test
    void testLoescheKleidungsstueck() {
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .pathParam("id", this.testKleidungsstueck.kleidungsId)
                .delete();

        Kleidungsstueck kleidung = this.kleidungsstueckKatalog
                .gebeKleidungsstueckVomBenutzerMitId(this.testKleidungsstueck.kleidungsId, benutzername);
        Assert.assertTrue(kleidung == null);
    }
}
