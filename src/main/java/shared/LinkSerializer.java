package shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Link;

public class LinkSerializer extends JsonSerializer<List<Link>>{

    @Override
    public void serialize(List<Link> links, JsonGenerator jsonGenerator, SerializerProvider sp) 
            throws IOException, JsonProcessingException {
                if (links == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeStartArray();
                    for (Link link : links) {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("uri", link.getUri().toString());
                        jsonGenerator.writeStringField("rel", link.getRel());
                        jsonGenerator.writeEndObject();
                    }
                    jsonGenerator.writeEndArray();
                }
            }
}