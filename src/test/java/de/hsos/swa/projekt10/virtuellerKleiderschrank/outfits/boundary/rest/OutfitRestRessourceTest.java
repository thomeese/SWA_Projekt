package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitFilter;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitOutputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.gateway.repository.OutfitRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@QuarkusTest
@TestHTTPEndpoint(OutfitRestRessource.class)
class OutfitRestRessourceTest {
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

    private List<OutfitOutputDTO> testOutfits;

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
        List<KleidungsstueckOutputDTO> testKleidungsstuecke;
        List<Kleidungsstueck> kleidungsstuecke = kleidungsstueckKatalog
                .gebeAlleKleidungsstueckeVomBenutzer(new KleidungsstueckFilter(), benutzername);
        testKleidungsstuecke = kleidungsstuecke.stream().map(kleidungsstueck -> kDtoKonverter.konvert(kleidungsstueck))
                .toList();
        OutfitInputDTO o1 = new OutfitInputDTO(benutzername, Arrays.asList("Sommer"),
                Arrays.asList(kleidungsstuecke.get(1).getKleidungsId(), kleidungsstuecke.get(2).getKleidungsId()),
                false);
        OutfitInputDTO o2 = new OutfitInputDTO(benutzername, Arrays.asList("Winter"),
                Arrays.asList(kleidungsstuecke.get(0).getKleidungsId(), kleidungsstuecke.get(2).getKleidungsId()),
                false);

        this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(o1, benutzername);
        this.outfitKatalog.erstelleOutfitFuerEinenBenutzer(o2, benutzername);
        // Lese die Beispiel-Daten aus der DatenbankS
        this.testOutfits = this.outfitKatalog.gebeAlleOutfitsVomBenutzer(new OutfitFilter(), benutzername)
                .stream().map(outfit -> OutfitOutputDTO.Converter.toOutfitOutputDTO(outfit)).toList();

    }

    @AfterEach
    public void cleanUp() {
        this.kleidungsstueckKatalog.loescheAlleKleidungsstueckeEinesBenutzers(benutzername);
        this.outfitKatalog.loescheAlleOutfitsEinesBenutzers(benutzername);
    }

    @Test
    void getAlleOutfits() {
        String response = given()
                .auth()
                .oauth2(this.service.gebeAccessToken(benutzername, passwort))
                .when()
                .get()
                .then()
                .extract()
                .body()
                .asString();

        List<OutfitOutputDTO> erhalteneOutfits = new ArrayList<>();
        try {
            erhalteneOutfits = objectMapper.readValue(response,new TypeReference<List<OutfitOutputDTO>>() {});
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        boolean test = true;
        int index = 0;
        if (this.testOutfits.size() != erhalteneOutfits.size()) {
            Assert.assertTrue(false);
        }
        while (index < this.testOutfits.size()) {
            if (!testOutfits.get(index).equals(erhalteneOutfits.get(index))) {
                test = false;
            }
            index++;
        }

        Assert.assertTrue(test);
    }

    @Test

    void erstelleOutfit(){
        OutfitInputDTO dto = new OutfitInputDTO("test", new ArrayList<String>(), new ArrayList<Long>(), false);
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken("testbenutzer", "test"))
                .body(dto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    void loescheAlleOutfits() {
        given()
                .auth()
                .oauth2(this.service.gebeAccessToken("testbenutzer", "test"))
                .when()
                .delete()
                .then()
                .statusCode(200);
    }
}