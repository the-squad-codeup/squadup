package pro.squadup.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Comrade;
import pro.squadup.models.Recruit;
import pro.squadup.models.User;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RecruitController {

    private UserRepository userDao;
    private RecruitRepository recruitDao;

    private ComradeRepository comradeDao;



    public RecruitController(UserRepository userDao, RecruitRepository recruitDao, ComradeRepository comradeDao) {
        this.userDao = userDao;
        this.recruitDao = recruitDao;
        this.comradeDao = comradeDao;
    }

    @GetMapping("/recruits/all")
    public Set<Recruit> getAllByUser(){
        User user = userDao.findById(Utils.currentUserId()).get();
        // Set<Recruit> allRecruits
        // Set<Recruit> trimmedRecruits = new HashSet<>();

        // enhance for loop to check every single recruit in allRecruits
        // if recruit either isAccepted or isRejected, do nothing
        // if neither is true, add to trimmedRecruits

        // return trimmedRecruits
        return new HashSet<>(recruitDao.findAllByUserOne(user));
    }

    @PostMapping("/recruits/{id}/accept")
    public Recruit acceptRecruit(@PathVariable Long id){
        User user = userDao.findById(Utils.currentUserId()).get();
        Recruit recruit = recruitDao.findById(id).get();
        User inverseUser = recruit.getUserTwo();
        Recruit inverseRecruit = recruitDao.findByUserOneAndUserTwo(inverseUser, user);
        recruit.setAccepted(true);
        recruitDao.save(recruit);
        if (inverseRecruit.isAccepted()){
            Timestamp currentTimeAndDate = new Timestamp(new Date().getTime());
            Comrade comrade = new Comrade(currentTimeAndDate, user, inverseUser);
            Comrade inverseComrade = new Comrade(currentTimeAndDate, inverseUser, user);
            comradeDao.save(comrade);
            comradeDao.save(inverseComrade);
        }
        return recruit;
    }


    @PostMapping("/recruits/{id}/reject")
    public Recruit rejectRecruit(@PathVariable Long id){
        Recruit recruit = recruitDao.findById(id).get();
        recruit.setRejected(true);
        recruitDao.save(recruit);
        return recruit;
    }

}
