package de.hsos.swa.projekt10.virtuellerKleiderschrank;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.test.keycloak.client.KeycloakTestClient;

@ApplicationScoped
public class KeycloakTestTokenService {
    KeycloakTestClient keycloak = new KeycloakTestClient();
    private static String clientId = "quarkus";
    
    public String gebeAccessToken(String benutzername, String passwort){
        return this.keycloak.getAccessToken(benutzername, passwort, clientId).toString();
    }
}
