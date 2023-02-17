package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;


import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.KeycloakTestTokenService;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.dto.DTOKonverter;
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

    private long kleidungsId;

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

    }

    @Test
    void testEntferneKategorie() {

    }

    @Test
    void testFuegeKategorieHinzu() {

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
        .body()
        .asString();

        List<KleidungsstueckOutputDTO> erhalteneKleidungsstuecke= null;

        try{
        erhalteneKleidungsstuecke = objectMapper.readValue(response, new TypeReference<List<KleidungsstueckOutputDTO>>(){});;
        }catch(Exception ex){
            Assert.assertTrue(false);
        }
        if(erhalteneKleidungsstuecke.size() > 1){
            Assert.assertTrue(false);
        }
        Assert.assertTrue(erhalteneKleidungsstuecke.get(0).equals(this.testKleidungsstueck));
    }

    @Test
    void testLoescheKleidungsstueck() {

    }
}
