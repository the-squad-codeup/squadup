package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.*;
import pro.squadup.repositories.*;
import pro.squadup.services.GameApiService;
import pro.squadup.services.RecruitMatchingService;
import pro.squadup.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepository gameDao;
    private final GenreRepository genreDao;
    private final PlatformRepository platformDao;
    private final PlatformMappingRepository platformMappingDao;
    private final RatingRepository ratingDao;
    private final UserRepository userDao;
    private final PreferencesRepository preferencesDao;

    @Autowired
    private GameApiService gameApiService;

    @Autowired
    private RecruitMatchingService recruitMatchingService;

    public GameController
                        (
                                GameRepository gameDao,
                                GenreRepository genreDao,
                                PlatformRepository platformDao,
                                PlatformMappingRepository platformMappingDao,
                                RatingRepository ratingDao, UserRepository userDao, PreferencesRepository preferencesDao)
    {
        this.gameDao = gameDao;
        this.genreDao = genreDao;
        this.platformDao = platformDao;
        this.platformMappingDao = platformMappingDao;
        this.ratingDao = ratingDao;
        this.userDao = userDao;
        this.preferencesDao = preferencesDao;
    }


    @PostMapping("/search")
    public List<Game> searchGames(@RequestBody String query) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = userDao.findById(Utils.currentUserId()).get();
        List<Game> allGames = scrapeGamesInfo(gameApiService.searchGames(query));
        List<Game> trimmedGames = new ArrayList<>();
        for(Game game : allGames) {
            Game scrapedGame = scrapeGameInfo(game);
            System.out.println(mapper.writeValueAsString(scrapedGame));
            if(gameMatchesUserPreferences(scrapedGame, user)) {
                trimmedGames.add(scrapedGame);
            }
        }
        return trimmedGames;
    }

    @PostMapping("/{igdbId}/add")
    public Game addGame(@PathVariable long igdbId) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Game game = scrapeGameInfo(gameApiService.addGame(igdbId));

        Set<Game> userGames = currentUser.getPreferences().getGames();
        if(!userGames.contains(game)) {
            Set<Genre> userGenres = currentUser.getPreferences().getGenres();
            for(Genre genre : game.getGenres()) {
                if(!userGenres.contains(genre)) {
                    userGenres.add(genre);
                }
            }
            currentUser.getPreferences().setGenres(userGenres);
            userGames.add(game);
            currentUser.getPreferences().setGames(userGames);
            preferencesDao.save(currentUser.getPreferences());
            userDao.save(currentUser);
            recruitMatchingService.matchAllRecruits();
        }
        return game;
    }

    private List<Game> scrapeGamesInfo(List<Game> igdbGames) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        List<Game> games = new ArrayList<>();
        for(Game game : igdbGames) {
            games.add(scrapeGameInfo(game));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("scrapeGamesInfo complete in " + (endTime - startTime) + "ms");
        return games;
    }

    private Game scrapeGameInfo(Game game) throws JsonProcessingException {
        // Sets game to existing game in database if it already exists, or creates new game
        if(gameDao.existsByIgdbId(game.getIgdbId())) {
            game = gameDao.findByIgdbId(game.getIgdbId());
        } else {
            setGameGenres(game);
            setGamePlatforms(game);
            setGameRating(game);
        }
        return game;
    }

    private void setGameGenres(Game game) {
        // Checks current genres in database and adds any that are not present
        Set<Genre> genres = new HashSet<>();
        for (Genre genre : game.getGenres()) {
            if (genreDao.existsByName(genre.getName())) {
                genres.add(genreDao.findByName(genre.getName()));
            } else {
                genres.add(genreDao.save(genre));
            }
        }
        game.setGenres(genres);
    }

    private void setGamePlatforms(Game game) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("inside setGamePlatforms");
        // Sets game platform based on the platforms in our database
        Set<Platform> platforms = new HashSet<>();
        for (Platform platform : game.getPlatforms()) {
            System.out.println("Single platform in igdb style:");
            System.out.println(mapper.writeValueAsString(platform));
            Long mappingId = platform.getIgdbIds().stream().findFirst().get().getIgdbId();
            Platform platformToAdd = platformDao.findByIgdbIdsIgdbId(mappingId);
            if (
                    platformMappingDao.existsByIgdbId(mappingId) &&
                            platformDao.existsByIgdbIdsIgdbId(mappingId) &&
                            !platforms.contains(platformToAdd)
            ) {
                platforms.add(platformToAdd);
            }
        }
        System.out.println("All platforms in our style:");
        System.out.println(mapper.writeValueAsString(platforms));
        game.setPlatforms(platforms);
    }

    private void setGameRating(Game game) {
        Rating rating;
        if(game.getRating() != null) {
            rating = ratingDao.findByIgdbId(game.getRating().getIgdbId());
        } else {
            rating = ratingDao.findByIgdbId(6);
        }
        game.setRating(rating);
    }

    private boolean gameMatchesUserPreferences(Game game, User user) {
        boolean doesMatch = false;
        if(
                platformMatches(game, user) &&
                ratingMatches(game, user)
        ) {
            doesMatch = true;
        }
        return doesMatch;
    }

    private boolean platformMatches(Game game, User user) {
        for(Platform gamePlatform : game.getPlatforms()) {
            if(user.getPreferences().getPlatforms().contains(gamePlatform)) {
                return true;
            }
        }
        return false;
    }

    private boolean ratingMatches(Game game, User user) {
        if(user.getPreferences().getRating().getId() >= game.getRating().getId()) {
            return true;
        }
        return false;
    }
}
