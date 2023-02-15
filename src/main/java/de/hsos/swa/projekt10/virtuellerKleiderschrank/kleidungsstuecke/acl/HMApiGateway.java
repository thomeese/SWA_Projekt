package de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import de.hsos.swa.projekt10.virtuellerKleiderschrank.kleidungsstuecke.acl.dto.KleidungsstueckHMDTO;
import io.quarkus.rest.client.reactive.ClientQueryParam;

@RegisterRestClient(configKey = "hm-api")
@ClientHeaderParam(name="X-Rapidapi-Key", value="${header-hm-api.rapidapi-key}")
@ClientHeaderParam(name="X-Rapidapi-Host", value="${header-hm-api.rapidapi-host}")
@ClientQueryParam(name="lang", value="de")
@ClientQueryParam(name="country", value="de")
public interface HMApiGateway {
    
    @GET
    @Path("/products/detail")
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(
        maxRetries = 4 ,
        delay = 250
    )
    @CircuitBreaker(
        requestVolumeThreshold=4 
    )
    KleidungsstueckHMDTO getKleidungsstueckByArtikelnummer(@QueryParam("productcode") String artikelnummer);
}
