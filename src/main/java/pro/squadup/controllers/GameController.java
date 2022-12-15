package pro.squadup.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.repositories.GameRepository;
import pro.squadup.repositories.GenreRepository;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepository gameDao;
    private final GenreRepository genreDao;

    public GameController(GameRepository gameDao, GenreRepository genreDao) {
        this.gameDao = gameDao;
        this.genreDao = genreDao;
    }


}
