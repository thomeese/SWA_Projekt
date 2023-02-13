package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.Kleidungsstueck;
import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.entity.KleidungsstueckKatalog;
@ApplicationScoped
public class KleidungsstueckInformationsAdapter implements KleidungsstueckInformation{
    @Inject
    public KleidungsstueckKatalog kKatalog;
    @Override
    public KleidungsstueckInformationsDTO gebeKleidungsstueckInforamtionen(long kleidungsId, String benutzername) {
        Kleidungsstueck kleidung = this.kKatalog.gebeKleidungsstueckVomBenutzerMitId(kleidungsId,benutzername);
        if(kleidung == null){
            return null;
        }
        return new KleidungsstueckInformationsDTO(kleidung.getName(),kleidung.getKleidungsId());
    }
    
}
