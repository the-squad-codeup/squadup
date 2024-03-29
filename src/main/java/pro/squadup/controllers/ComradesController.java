package pro.squadup.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Comrade;
import pro.squadup.models.User;
import pro.squadup.repositories.ComradeRepository;
import pro.squadup.repositories.RecruitRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.util.Set;

@RestController
public class ComradesController {

    private UserRepository userDao;
    private ComradeRepository comradeDao;

    public ComradesController(UserRepository userDao, ComradeRepository comradeDao) {
        this.userDao = userDao;
        this.comradeDao = comradeDao;
    }

    // Returns all comrades of currently logged in user
    @GetMapping("/comrades/all")
    public Set<Comrade> getAllByUser(){
        User user = userDao.findById(Utils.currentUserId()).get();
        return user.getComrades();
    }

    // deletes comrade by id included in path after verifying current user is deleting their comrade
    @PostMapping("/comrades/{id}/delete")
    public void deleteComrade(@PathVariable Long id){
        User user = userDao.findById(Utils.currentUserId()).get();
        Comrade comrade = comradeDao.findById(id).get();
        if(user.getId() == comrade.getUserOne().getId()) {
            User inverseUser = comrade.getUserTwo();
            Comrade inverseComrade = comradeDao.findByUserOneAndUserTwo(inverseUser, user);
            comradeDao.delete(comrade);
            comradeDao.delete(inverseComrade);
        }
    }


}
