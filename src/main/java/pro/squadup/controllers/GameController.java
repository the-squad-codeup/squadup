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


    @PostMapping("/search")
    public List<Game> searchGames(@RequestBody String query) throws IOException {
        List<Game> allGames = gameApiService.searchGames(query);
        List<Game> trimmedGames = new ArrayList<>();
        for(Game game : allGames) {

        }
        return gameApiService.searchGames(query);
    }

    @PostMapping("/{igdbId}/add")
    public Game addGame(@PathVariable long igdbId) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Game game;

        // Sets game to existing game in database if it already exists, or creates new game
        if(gameDao.existsByIgdbId(igdbId)) {
            game = gameDao.findByIgdbId(igdbId);
        } else {
            game = gameApiService.addGame(igdbId);

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

            Rating rating;
            if(game.getRating() != null) {
                rating = ratingDao.findByIgdbId(game.getRating().getIgdbId());
            } else {
                rating = ratingDao.findByIgdbId(6);
            }
            game.setRating(rating);

            gameDao.save(game);
        }

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
}
