package shared;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class LinkDeserializer extends JsonDeserializer<List<Link>> {
    
    
    @Override
    public List<Link> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        List<Link> links = new ArrayList<>();
        JsonNode node = parser.getCodec().readTree(parser);
        for (JsonNode linkNode : node) {
            Link link = Link.fromUri(linkNode.get("href").asText()).rel(linkNode.get("rel").asText())
            .title(linkNode.get("title").asText()).param("method", linkNode.get("method").asText()).build();
            links.add(link);
        }
        return links;
    }
}
