package de.hsos.swa.projekt10.virtuellerKleiderschrank;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@OpenAPIDefinition(
    tags = {
            @Tag(name="Kleidungsstuecke", description="Generelle Operationen fuer Kleidungsstuecke."),
            @Tag(name="Outfits", description="Generelle Operationen fuer Outfits.")
    },
    info = @Info(
        title="Virtueller Kleiderschrank API",
        version="1.0"
    )
)
public class VirtuellerKleiderschrankApplication extends Application {
}
