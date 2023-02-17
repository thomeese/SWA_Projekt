package de.hsos.swa.projekt10.virtuellerKleiderschrank;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.test.keycloak.client.KeycloakTestClient;

@ApplicationScoped
public class KeycloakTestTokenService {
    KeycloakTestClient keycloak = new KeycloakTestClient();
    private static String clientId = "quarkus";
    /**
     * Generiert einen Acces Token, wird fuer die Tests benoetigt.
     * 
     * @param benutzername Credential:username
     * @param passwort Credential:password
     * @return String Access Token
     */
    public String gebeAccessToken(String benutzername, String passwort){
        return this.keycloak.getAccessToken(benutzername, passwort, clientId).toString();
    }
}
