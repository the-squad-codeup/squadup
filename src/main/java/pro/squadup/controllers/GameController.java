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

    // returns current user's games
    @GetMapping("/user")
    public Set<Game> getMyGames(){
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return currentUser.getPreferences().getGames();
    }

    // returns list of games based on search query string
    // calls IGDB api to populate list
    // filters based on current user's preferences
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

    // returns user's favorite game
    @GetMapping("/favorite")
    public Game getFavoriteGame() {
        User user = userDao.findById(Utils.currentUserId()).get();
        Game favoriteGame = user.getPreferences().getFavoriteGame();
        if(favoriteGame != null) {
            return favoriteGame;
        }
        return new Game();
    }

    // sets user's favorite game by game id
    @PostMapping("/{gameId}/favorite")
    public Game setFavoriteGame(@PathVariable Long gameId) {
        User user = userDao.findById(Utils.currentUserId()).get();
        Game gameToFavorite = gameDao.findById(gameId).get();
        user.getPreferences().setFavoriteGame(gameToFavorite);
        userDao.save(user);
        return gameToFavorite;
    }

    // adds game based on game id
    // will only add if game is not already in user's game list
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
            recruitMatchingService.matchRecruits(currentUser, new HashSet<>(userDao.findAll()));
        }
        return game;
    }

    // removes games from current user's game list
    // does not delete game from database
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

    // gets list of game objects from IGDB api and calls method to scrape each game for only info we store in database
    private List<Game> scrapeGamesInfo(List<Game> igdbGames) {
        List<Game> games = new ArrayList<>();
        for(Game game : igdbGames) {
            games.add(scrapeGameInfo(game));
        }
        return games;
    }

    // scrapes info we use from IDGB api games
    // saves game to database if not already saved
    private Game scrapeGameInfo(Game game) {
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

    // Checks current genres in database and adds any that are not present
    private void setGameGenres(Game game) {
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

    // Sets game platform based on the platforms in our database
    private void setGamePlatforms(Game game) {
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

    // sets correct game rating based on how it is saved in database
    private void setGameRating(Game game) {
        Rating rating;
        if(game.getRating() != null) {
            rating = ratingDao.findByIgdbId(game.getRating().getIgdbId());
        } else {
            rating = ratingDao.findByIgdbId(6);
        }
        game.setRating(rating);
    }

    // returns true or false if game matches user's preferences
    private boolean gameMatchesUserPreferences(Game game, User user) {
        if(
                platformMatches(game, user) &&
                ratingMatches(game, user)
        ) {
            return true;
        }
        return false;
    }

    // returns true or false based on whether game title contains the word "edition"
    private boolean notGameEdition(Game game) {
        if(game.getTitle().contains("Edition")) {
            return false;
        }
        return true;
    }

    // returns true or false based on if game platform matches user's platforms
    private boolean platformMatches(Game game, User user) {
        for(Platform gamePlatform : game.getPlatforms()) {
            if(user.getPreferences().getPlatforms().contains(gamePlatform)) {
                return true;
            }
        }
        return false;
    }

    // returns true or false based on whether game age rating matches user's age rating preference
    private boolean ratingMatches(Game game, User user) {
        if(user.getPreferences().getRating().getId() >= game.getRating().getId()) {
            return true;
        }
        return false;
    }
}
