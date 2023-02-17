package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import org.junit.jupiter.api.Test;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.KeycloakTestTokenService;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.OutfitKatalog;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@QuarkusTest
@TestHTTPEndpoint(OutfitRestRessource.class)
class OutfitRestRessourceTest {
    @Inject
    KeycloakTestTokenService service;

    


    @Test
    void getAlleOutfits() {
        given()
        .auth()
        .oauth2(this.service.gebeAccessToken("testbenutzer", "test"))
        .when()
        .get()
        .then()
        .statusCode(200);
    }

    @Test
    void erstelleOutfit(){
        OutfitInputDTO dto = new OutfitInputDTO("test", new ArrayList<String>(), new ArrayList<Long>(), false);
        given()
        .auth()
        .oauth2(this.service.gebeAccessToken("testbenutzer", "test"))
        .body(dto)
        .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON)
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