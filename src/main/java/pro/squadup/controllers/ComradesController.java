package pro.squadup.controllers;


import org.springframework.web.bind.annotation.RestController;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;

@RestController
public class ComradesController {

    private UserRepository userDao;

    private RecruitRepository recruitDao;

    private ComradeRepository comradeDao;

    public ComradesController(UserRepository userDao, RecruitRepository recruitDao, ComradeRepository comradeDao) {
        this.userDao = userDao;
        this.recruitDao = recruitDao;
        this.comradeDao = comradeDao;
    }



}
