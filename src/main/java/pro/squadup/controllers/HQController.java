package pro.squadup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;

@RestController
public class HQController {

    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final ComradeRepository comradeDao;

    public HQController(UserRepository userDao, SquadRepository squadDao, ComradeRepository comradeDao) {
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.comradeDao = comradeDao;
    }



}
