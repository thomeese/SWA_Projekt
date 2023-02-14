package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.gateway;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.NutzerDTO;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.control.Benutzerverwaltung;
@ApplicationScoped
public class BenutzerGateway implements Benutzerverwaltung{
    @Inject
    Keycloak keycloak;

    private static String realmName = "quarkus";
    private static String roleName = "benutzer";
    @Override
    public boolean legeBenutzerkontoAn(NutzerDTO nutzerdaten) {
        //Nutzer erstellen
        UserRepresentation nutzer = new UserRepresentation();
        nutzer.setUsername(nutzerdaten.benutzername);
        nutzer.setEnabled(true);
        nutzer.setFirstName(nutzerdaten.vorname);
        nutzer.setLastName(nutzerdaten.nachname);
        nutzer.setEmail(nutzerdaten.email);
        try{
            RealmResource realm = this.keycloak.realm(realmName);
            UsersResource userResource =  realm.users();
            Response response = userResource.create(nutzer);
            //Userid bestimmen
            String userId = response.getLocation().getPath().substring(response.getLocation().getPath().lastIndexOf("/") +1);
            //Benutzer Rolle zuweisen
            RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
            userResource.get(userId).roles().realmLevel().add(Arrays.asList(role));
            //Credentials generieren
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(nutzerdaten.passwort);
            //Credential Setzen
            userResource.get(userId).resetPassword(credential);
            return true;
        }catch(Exception exception){
            return false;
        }
        
       
    }
    
}
