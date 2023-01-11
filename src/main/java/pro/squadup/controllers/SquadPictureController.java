package pro.squadup.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadPicture;
import pro.squadup.models.User;
import pro.squadup.repositories.SquadPictureRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@RestController
public class SquadPictureController {

    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final SquadPictureRepository squadPictureDao;

    public SquadPictureController(UserRepository userDao, SquadRepository squadDao, SquadPictureRepository squadPictureDao) {
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.squadPictureDao = squadPictureDao;
    }

    @PostMapping("/squads/{squadId}/picture")
    public SquadPicture setSquadPicture(@PathVariable Long squadId, @RequestBody SquadPicture squadPicture) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        SquadPicture currentPicture = squadPictureDao.findBySquad(squad);
        if(squad.getOwner().equals(currentUser)) {
            squad.setSquadPicture(squadPicture);
            squadPictureDao.save(squadPicture);
            squadDao.save(squad);
            squadPictureDao.delete(currentPicture);
            return squadPicture;
        }
        return currentPicture;
    }
}
