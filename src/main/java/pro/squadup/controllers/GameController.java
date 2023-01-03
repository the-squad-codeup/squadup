package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @GetMapping("/user")
    public Set<Game> userGames(){
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return currentUser.getPreferences().getGames();
    }

    @PostMapping("/search")
    public List<Game> searchGames(@RequestBody String query) throws IOException {
        User user = userDao.findById(Utils.currentUserId()).get();
        List<Game> allGames = scrapeGamesInfo(gameApiService.searchGames(query));
        List<Game> trimmedGames = new ArrayList<>();
        for(Game game : allGames) {
            Game scrapedGame = scrapeGameInfo(game);
            if(gameMatchesUserPreferences(scrapedGame, user) && notGameEdition(scrapedGame)) {
                trimmedGames.add(scrapedGame);
            }
        }
        return trimmedGames;
    }

    @GetMapping("/favorite")
    public Game getFavoriteGame() {
        User user = userDao.findById(Utils.currentUserId()).get();
        return user.getPreferences().getFavoriteGame();
    }

    @PostMapping("/{gameId}/add")
    public Game addGame(@PathVariable long gameId) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Game game = gameDao.findById(gameId).get();

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

    @PostMapping("/{gameId}/remove")
    public Game removeGame(@PathVariable Long gameId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Set<Game> updatedGames = currentUser.getPreferences().getGames();
        Game gameToRemove = gameDao.findById(gameId).get();
        if(currentUser.getPreferences().getGames().contains(gameToRemove)) {
            updatedGames.remove(gameToRemove);
        }
        currentUser.getPreferences().setGames(updatedGames);
        userDao.save(currentUser);
        return gameToRemove;
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
            gameDao.save(game);
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
        // Sets game platform based on the platforms in our database
        Set<Platform> platforms = new HashSet<>();
        for (Platform platform : game.getPlatforms()) {
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
        if(
                platformMatches(game, user) &&
                ratingMatches(game, user)
        ) {
            return true;
        }
        return false;
    }

    private boolean notGameEdition(Game game) {
        if(game.getTitle().contains("Edition")) {
            return false;
        }
        return true;
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
