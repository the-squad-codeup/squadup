package pro.squadup.controllers;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/squads/{squadId}/picture")
    public SquadPicture getSquadPicture(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squad.getSquadPicture();
    }

    @PostMapping("/squads/{squadId}/picture")
    public SquadPicture setSquadPicture(@PathVariable Long squadId, @RequestBody SquadPicture squadPicture) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        if(!squad.getOwner().equals(currentUser)) {
            return null;
        }
        SquadPicture currentPicture = squad.getSquadPicture();
        squadPictureDao.save(squadPicture);
        squad.setSquadPicture(squadPicture);
        squadDao.save(squad);
        currentPicture.setSquad(null);
        squadPictureDao.deleteById(currentPicture.getId());
        return squadPicture;
    }
}
