package de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.control;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.benutzer.boundary.NutzerDTO;

public interface Benutzerverwaltung {
   /**
    * Legt fuer den Nutzer ein neues Benutzerkonto an. Das Benutzer
    + Objekt wird dabei in der Keycloak Datenbank persistiert. Fuer
    * die Komminaktion mit Keycloak wird der Keycloak Admin-Client
    * verwendet.
    * @param nutzerdaten Fuer die Erstellung benoetige Benutzerdaten
    * @return boolean true bei Erfolg, ansonsten false
    * @author Thomas Meese
    */
   public boolean legeBenutzerkontoAn(NutzerDTO nutzerdaten);
}
