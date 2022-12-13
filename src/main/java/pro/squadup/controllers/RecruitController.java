package pro.squadup.controllers;

import org.springframework.web.bind.annotation.RestController;
import pro.squadup.repositories.UserRepository;

@RestController
public class RecruitController {

    private UserRepository userDao;

    public RecruitController(UserRepository userDao) {
        this.userDao = userDao;
    }




}
