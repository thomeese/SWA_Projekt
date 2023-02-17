package de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.rest;

import java.net.URI;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.mapper.Mapper.Null;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.KeycloakTestTokenService;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.boundary.dto.OutfitInputDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.outfits.entity.Outfit;
import groovy.xml.Entity;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
//@TestHTTPEndpoint(OutfitIdRestRessource.class)
public class OutfitIdRestRessourceTest {
    @Inject
    KeycloakTestTokenService service;

    @Inject
    EntityManager em;

    private static final String benutzername = "testbenutzer";
    private static final String passwort = "test";

    @Test
    void testBearbeiteOutfit() {
        OutfitInputDTO erstellDto = new OutfitInputDTO("neuer Name", new ArrayList<String>(), new ArrayList<Long>(), false);
        OutfitInputDTO dto = new OutfitInputDTO("neuer Name", new ArrayList<String>(), new ArrayList<Long>(), false);
        String s =given()
        .auth()
        .oauth2(this.service.gebeAccessToken(benutzername, passwort))
        .body(dto)
        .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON)
        .when()
        .post("/virtuellerkleiderschrank/v1/api/outfits")
        .then()
        .extract()
        .body()
        .asString();

        System.out.println(s);
        Logger.getLogger(OutfitIdRestRessourceTest.class).debugf("Der Reposone ist", s);
        given()
        .auth()
        .oauth2(this.service.gebeAccessToken(benutzername, passwort))
        .body(dto)
        .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON)
        .when()
        .pathParam("id",1)
        .patch("/virtuellerkleiderschrank/v1/api/outfits")
        .then()
        .statusCode(200);
        //this.em.remove(outfit);
    }

    @Test
    void testEntferneKleidungsstueck() {

    }

    @Test
    void testGetOutfit() {

    }

    @Test
    void testLoescheOutfit() {

    }

    @Test
    void testTeileOutfit() {

    }
}
