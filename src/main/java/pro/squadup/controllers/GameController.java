package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.squadup.repositories.GameRepository;
import pro.squadup.repositories.GenreRepository;
import pro.squadup.services.GameApiService;

import java.io.IOException;

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
    public void searchGames(@RequestBody String query) throws IOException {
        System.out.println("Inside searchGames. Query string: ");
        System.out.println(query);
        gameApiService.searchGames(query);
    }
}
