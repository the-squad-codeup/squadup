package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.repositories.GameRepository;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.services.GameApiService;

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


    @PostMapping("/{query}")
    public void searchGames(@PathVariable String query) throws JsonProcessingException {
        gameApiService.searchGames(query);
    }
}
