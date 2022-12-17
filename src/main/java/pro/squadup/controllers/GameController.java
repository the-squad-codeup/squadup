package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.PlatformMapping;
import pro.squadup.models.Game;
import pro.squadup.models.Genre;
import pro.squadup.models.Platform;
import pro.squadup.repositories.GameRepository;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.repositories.PlatformMappingRepository;
import pro.squadup.repositories.PlatformRepository;
import pro.squadup.services.GameApiService;

import java.io.IOException;
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

    @Autowired
    private GameApiService gameApiService;

    public GameController
                        (
                        GameRepository gameDao,
                        GenreRepository genreDao,
                        PlatformRepository platformDao,
                        PlatformMappingRepository platformMappingDao
                        )
    {
        this.gameDao = gameDao;
        this.genreDao = genreDao;
        this.platformDao = platformDao;
        this.platformMappingDao = platformMappingDao;
    }


    @PostMapping("/search")
    public List<Object> searchGames(@RequestBody String query) throws IOException {
        System.out.println("Inside searchGames. Query string: ");
        System.out.println(query);
        return gameApiService.searchGames(query);
    }

    @PostMapping("/{igdbId}/add")
    public Game addGame(@PathVariable long igdbId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Inside addGame. Game ID: ");
        System.out.println(igdbId);

        if(gameDao.existsByIgdbId(igdbId)) {
            return gameDao.findByIgdbId(igdbId);
        }

        Game game = gameApiService.addGame(igdbId);
        Set<Genre> genres = new HashSet<>();
        for(Genre genre : game.getGenres()) {
            if(genreDao.existsByName(genre.getName())) {
                System.out.println("inside genre exists by name");
                genres.add(genreDao.findByName(genre.getName()));
            } else {
                genres.add(genreDao.save(genre));
            }
        }
        game.setGenres(genres);

        System.out.println("Setting platforms for game object.");
        Set<Platform> platforms = new HashSet<>();
        for(Platform platform : game.getPlatforms()) {
            System.out.println("Platform in game.getPlatforms()");
            System.out.println(mapper.writeValueAsString(platform));
            Long mappingId = platform.getIgdbIds().stream().findFirst().get().getIgdbId();
            System.out.println("mappingId: " + mappingId);
            Platform platformToAdd = platformDao.findByIgdbIdsIgdbId(mappingId);
            System.out.println("Platform to add:");
            System.out.println(mapper.writeValueAsString(platformToAdd));
            System.out.println("Platform mapping object exists by igdbId: " + platformMappingDao.existsByIgdbId(mappingId));
            System.out.println("Platform object exists by the igdbIds igdbId: " + platformDao.existsByIgdbIdsIgdbId(mappingId));
            System.out.println("Platforms set does not contain platform to add: " + !platforms.contains(platformToAdd));
            if(
                    platformMappingDao.existsByIgdbId(mappingId) &&
                    platformDao.existsByIgdbIdsIgdbId(mappingId) &&
                    !platforms.contains(platformToAdd)
            ) {
                System.out.println("inside if statement");
                platforms.add(platformToAdd);
            }
        }
        game.setPlatforms(platforms);




//        gameDao.save(game);
        return game;
    }
}
