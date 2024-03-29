package pro.squadup.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pro.squadup.models.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// Custom game deserializer to convert json string to Game object
public class GameDeserializer extends StdDeserializer<Game> {


    public GameDeserializer() {
        this(null);
    }

    public GameDeserializer(Class<?> vc) {
        super(vc);
    }

    // converts json string to Game object
    // overrides default jackson deserializer
    @Override
    public Game deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        // if node currently an array of one object, adjusts node object accordingly
        if(node.get(0) != null) {
            node = node.get(0);
        }

        // getting all game object properties from json object
        long igdbId = igdbIdFromNode(node);
        String title = titleFromNode(node);
        String artwork = artworkUrlFromNode(node);
        int year = yearFromNode(node);
        Rating rating = ratingFromNode(node);
        Set<Genre> genres = genresFromNode(node);
        Set<Platform> platforms = platformsFromNode(node);

        return new Game(igdbId, title, artwork, year, rating, genres, platforms);
    }

    // returns igdbId from Json object
    private long igdbIdFromNode(JsonNode node){
        // default value if null
        long igdbId = 0;
        if(node.get("id") != null) {
            igdbId = node.get("id").asLong();
        }
        return igdbId;
    }

    // returns title from Json object
    private String titleFromNode(JsonNode node) {
        // default value if null
        String title = "";
        if(node.get("name") != null) {
            title = node.get("name").asText();
        }
        return title;
    }

    // returns artwork url from Json object
    private String artworkUrlFromNode(JsonNode node) {
        // default value if null
        String artworkId = "";
        if(node.path("cover").get("image_id") != null){
            artworkId = node.path("cover").get("image_id").asText();
        }
        return "https://images.igdb.com/igdb/image/upload/t_cover_big/" + artworkId + ".jpg";
    }

    // returns year int from Json object
    private int yearFromNode(JsonNode node) {
        int year = 0;
        if(node.path("release_dates").get(0) != null && node.path("release_dates").get(0).get("y") != null) {
            year = node.path("release_dates").get(0).get("y").asInt();
        }
        return year;
    }

    // returns Rating object from Json object
    private Rating ratingFromNode(JsonNode node) {
        JsonNode ratingsNode = node.path("age_ratings");
        // default value if null
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
        return new Rating(igdbRatingId);
    }

    // returns Genre Set from Json object
    private Set<Genre> genresFromNode(JsonNode node) {
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
        return genres;
    }

    // returns Platform Set from Json object
    private Set<Platform> platformsFromNode(JsonNode node) {
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
        return platforms;
    }

}
