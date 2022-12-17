package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.Game;
import pro.squadup.models.Genre;
import pro.squadup.repositories.GameRepository;
import pro.squadup.repositories.GenreRepository;
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

    @Autowired
    private GameApiService gameApiService;

    public GameController(GameRepository gameDao, GenreRepository genreDao) {
        this.gameDao = gameDao;
        this.genreDao = genreDao;
    }


    @PostMapping("/search")
    public List<Object> searchGames(@RequestBody String query) throws IOException {
        System.out.println("Inside searchGames. Query string: ");
        System.out.println(query);
        return gameApiService.searchGames(query);
    }

    @PostMapping("/{igdbId}/add")
    public Game addGame(@PathVariable long igdbId) throws JsonProcessingException {
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


//        gameDao.save(game);
        return game;
    }
}
