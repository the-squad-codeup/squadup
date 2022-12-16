package pro.squadup.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Recruit;
import pro.squadup.models.User;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RecruitController {

    private UserRepository userDao;
    private RecruitRepository recruitDao;



    public RecruitController(UserRepository userDao, RecruitRepository recruitDao) {
        this.userDao = userDao;
        this.recruitDao = recruitDao;
    }

    @GetMapping("/recruits/all")
    public Set<Recruit> getAllByUser(){
        User user = userDao.findById(Utils.currentUserId()).get();
        return new HashSet<>(recruitDao.findAllByUserOne(user));
    }

//    @GetMapping("/recruits/{id}/accept")



//    @GetMapping("/recruit/{id}/reject")


}
