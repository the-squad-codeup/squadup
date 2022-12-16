package pro.squadup.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import pro.squadup.models.Game;

import javax.persistence.criteria.Root;
import java.io.IOException;

public class GameDeserializer extends JsonDeserializer<Game> {
    @Override
    public Game deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        Root root = p.readValueAs(Root.class);


        return null;
    }
}
