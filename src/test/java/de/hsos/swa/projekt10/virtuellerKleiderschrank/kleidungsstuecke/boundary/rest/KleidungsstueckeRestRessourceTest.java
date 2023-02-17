package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.boundary.rest;

import java.util.ArrayList;
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

import static io.restassured.RestAssured.given;

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

@QuarkusTest
@TestHTTPEndpoint(KleidungsstueckeRestRessource.class)
class KleidungsstueckeRestRessourceTest {
    @Inject
    KeycloakTestTokenService service;

    @Inject
    KleidungsstueckRepository kleidungsstueckKatalog;

    @Inject
    DTOKonverter kDtoKonverter;

    @Inject
    ObjectMapper objectMapper;

    private List<KleidungsstueckOutputDTO> testKleidungsstuecke;

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

        kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k1, benutzername);

        kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k2, benutzername);

        kleidungsstueckKatalog.erstelleKleidungsstueckFuerBenutzer(k3, benutzername);

        // Lese die Beispiel-Daten aus der Datenbank
        List<Kleidungsstueck> kleidungsstuecke = kleidungsstueckKatalog
                .gebeAlleKleidungsstueckeVomBenutzer(new KleidungsstueckFilter(), benutzername);
        testKleidungsstuecke = kleidungsstuecke.stream().map(kleidungsstueck -> kDtoKonverter.konvert(kleidungsstueck))
                .toList();
    }

    @AfterEach
    public void cleanUp() {
        this.kleidungsstueckKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzername);
    }

    @Test
    public void testGetAlleKleidungsstuecke() {
        String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .get()
                .then()
                .extract()
                .body()
                .asString();

        List<KleidungsstueckOutputDTO> erhalteneKleidungsstuecke = new ArrayList<>();
        try {
            erhalteneKleidungsstuecke = objectMapper.readValue(response,
                    new TypeReference<List<KleidungsstueckOutputDTO>>() {
                    });
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        boolean test = true;
        int index = 0;
        if (testKleidungsstuecke.size() != erhalteneKleidungsstuecke.size()) {
            Assert.assertTrue(false);
        }
        while (index < testKleidungsstuecke.size()) {
            if (!testKleidungsstuecke.get(index).equals(erhalteneKleidungsstuecke.get(index))) {
                test = false;
            }
            index++;
        }

        Assert.assertTrue(test);
    }

    @Test
    void erstelleKleidungsstueck() {
        KleidungsstueckInputDTO dto = new KleidungsstueckInputDTO("L", Farbe.Blau, Typ.TShirt, "Test T-Shirt",
                new ArrayList<String>());
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .body(dto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when()
                .post();
        KleidungsstueckFilter filter = new KleidungsstueckFilter();
        filter.name="Test T-Shirt";
        //Pruefen ob das T-Shirt in der Datenbank liegt.
        Assert.assertTrue(this.kleidungsstueckKatalog.gebeAlleKleidungsstueckeVomBenutzer(filter, benutzername).size() == 1);

    }

    @Test
    void loescheAlleKleidungsstuecke() {
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .delete();

        int listsize = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("")
                .size();
        // Listengroesse muss hier gleich 0 sein da alle Kleidungsstuecke geloescht
        // wurden.
        Assert.assertTrue(listsize == 0);
    }
}