package pro.squadup.utils;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pro.squadup.models.*;

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

    @Override
    public Game deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        // Mapper just to sout and check if sets are correct

        JsonNode node = jp.getCodec().readTree(jp);
        node = node.get(0);
        long igdbId = 0;
        if(node.get("id") != null) {
            igdbId = node.get("id").asLong();
        }
        String title = "";
        if(node.get("name") != null) {
            title = node.get("name").asText();
        }
        String artworkId = "";
        if(node.path("cover").get("image_id") != null){
            artworkId = node.path("cover").get("image_id").asText();
        }
        String artwork = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + artworkId + ".jpg";
        JsonNode ratingsNode = node.path("age_ratings");
        int igdbRatingId = 6;
        if(ratingsNode.isArray()) {
            for(JsonNode ratingNode : ratingsNode) {
                int ratingCategory = (Integer) ratingNode.get("category").numberValue();
                if(ratingCategory == 1) {
                    igdbRatingId = (Integer) ratingNode.get("rating").numberValue();
                }
            }
        } else if(ratingsNode.path("age_ratings").get("category") != null){
            int ratingCategory = (Integer) ratingsNode.get("category").numberValue();
            if(ratingCategory == 1) {
                igdbRatingId = (Integer) ratingsNode.get("rating").numberValue();
            }
        }
        Rating rating = new Rating(igdbRatingId);
        JsonNode genresNode = node.path("genres");
        Set<String> genreNames = new HashSet<>();
        if(genresNode.isArray()) {
            for(JsonNode genreNode : genresNode) {
                String genreName = genreNode.get("name").asText();
                genreNames.add(genreName);
            }
        } else if(genresNode.get("name") != null) {
            String genreName = genresNode.get("name").asText();
            genreNames.add(genreName);
        }
        Set<Genre> genres = new HashSet<>();
        for(String genreName : genreNames) {
            genres.add(new Genre(genreName));
        }
        JsonNode platformsNode = node.path("platforms");
        Set<Long> igdbPlatformIds = new HashSet<>();
        if(platformsNode.isArray()) {
            for(JsonNode platformNode : platformsNode) {
                long igdbPlatformId = platformNode.get("id").asLong();
                igdbPlatformIds.add(igdbPlatformId);
            }
        } else if (platformsNode.get("id") != null){
            long igdbPlatformId = platformsNode.get("id").asLong();
            igdbPlatformIds.add(igdbPlatformId);
        }
        Set<Platform> platforms = new HashSet<>();
        for(Long igdbPlatformId : igdbPlatformIds) {
            Set<PlatformMapping> igdbPlatforms = new HashSet<>();
            igdbPlatforms.add(new PlatformMapping(igdbPlatformId));
            platforms.add(new Platform(igdbPlatforms));
        }

        Game game = new Game(igdbId, title, artwork, rating, genres, platforms);

        return game;
    }
}
