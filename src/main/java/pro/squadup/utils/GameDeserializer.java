package pro.squadup.utils;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.LongNode;
import pro.squadup.models.Game;

import java.io.IOException;

public class GameDeserializer extends StdDeserializer<Game> {

    public GameDeserializer() {
        this(null);
    }

    public GameDeserializer(Class<?> vc) {
        super(vc);
    }

//    @Override
//    public Item deserialize(JsonParser jp, DeserializationContext ctxt)
//            throws IOException, JsonProcessingException {
//        JsonNode node = jp.getCodec().readTree(jp);
//        int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        String itemName = node.get("itemName").asText();
//        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
//
//        return new Item(id, itemName, new User(userId, null));
//    }

    @Override
    public Game deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        System.out.println("Inside Game Deserializer");
        JsonNode node = jp.getCodec().readTree(jp);
        long igdbId = (Long) ((LongNode) node.get("id")).numberValue();
        System.out.println("igdbId: " + igdbId);
        String title = node.get("name").asText();
        System.out.println("title: " + title);
        String artworkId = node.path("cover").get("image_id").asText();
        System.out.println("artworkId: " + title);
        String artwork = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + artworkId + ".jpg";

        return null;
    }
}
