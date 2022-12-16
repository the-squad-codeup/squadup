package pro.squadup.utils;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import org.springframework.beans.factory.annotation.Autowired;
import pro.squadup.models.Game;
import pro.squadup.models.Genre;
import pro.squadup.models.Platform;
import pro.squadup.models.Rating;
import pro.squadup.services.DeserializerRepositoryService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
        // Mapper just to sout and check if sets are correct
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Inside Game Deserializer");
        JsonNode node = jp.getCodec().readTree(jp);
        node = node.get(0);
        System.out.println(mapper.writeValueAsString(node));
        long igdbId = node.get("id").asLong();
        System.out.println("igdbId: " + igdbId);
        String title = node.get("name").asText();
        System.out.println("title: " + title);
        String artworkId = node.path("cover").get("image_id").asText();
        System.out.println("artworkId: " + artworkId);
        String artwork = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + artworkId + ".jpg";
        JsonNode ratingsNode = node.path("age_ratings");
        int igdbRatingId = 0;
        if(ratingsNode.isArray()) {
            for(JsonNode ratingNode : ratingsNode) {
                int ratingCategory = (Integer) ratingNode.get("category").numberValue();
                if(ratingCategory == 1) {
                    igdbRatingId = (Integer) ratingNode.get("rating").numberValue();
                }
            }
        } else {
            int ratingCategory = (Integer) ratingsNode.get("category").numberValue();
            if(ratingCategory == 1) {
                igdbRatingId = (Integer) ratingsNode.get("rating").numberValue();
            }
        }
        System.out.println("igdbRatingId: " + igdbRatingId);
        Rating rating = new Rating(igdbRatingId);
        JsonNode genresNode = node.path("genres");
        Set<String> genreNames = new HashSet<>();
        if(genresNode.isArray()) {
            for(JsonNode genreNode : genresNode) {
                String genreName = genreNode.get("name").asText();
                genreNames.add(genreName);
            }
        } else {
            String genreName = genresNode.get("name").asText();
            genreNames.add(genreName);
        }
        System.out.println("genreNames: ");
        System.out.println(mapper.writeValueAsString(genreNames));
        Set<Genre> genres = new HashSet<>();
        for(String genreName : genreNames) {
            genres.add(new Genre(genreName));
        }
        JsonNode platformsNode = node.path("platforms");
        Set<Integer> igdbPlatformIds = new HashSet<>();
        if(platformsNode.isArray()) {
            for(JsonNode platformNode : platformsNode) {
                int igdbPlatformId = (Integer) platformNode.get("id").numberValue();
                igdbPlatformIds.add(igdbPlatformId);
            }
        } else {
            int igdbPlatformId = (Integer) platformsNode.get("id").numberValue();
            igdbPlatformIds.add(igdbPlatformId);
        }
        System.out.println("igdbPlatformIds: ");
        System.out.println(mapper.writeValueAsString(igdbPlatformIds));
        Set<Platform> platforms = new HashSet<>();
        for(Integer igdbPlatformId : igdbPlatformIds) {
            platforms.add(new Platform(igdbPlatformId));
        }

        Game game = new Game(igdbId, title, artwork, rating, genres, platforms);
        System.out.println(mapper.writeValueAsString(game));

        return game;
    }
}
