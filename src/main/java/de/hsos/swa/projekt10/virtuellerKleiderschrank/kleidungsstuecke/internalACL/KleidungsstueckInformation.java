package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.internalACL;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface KleidungsstueckInformation {
    public KleidungsstueckInformationsDTO gebeKleidungsstueckInforamtionen(long kleidungsId);
}
