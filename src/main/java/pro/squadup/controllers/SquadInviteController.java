package pro.squadup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.SquadInvite;
import pro.squadup.models.User;
import pro.squadup.repositories.SquadInviteRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.util.Set;

@RestController
public class SquadInviteController {

    private final UserRepository userDao;
    private final SquadInviteRepository squadInviteDao;

    public SquadInviteController(UserRepository userDao, SquadInviteRepository squadInviteDao) {
        this.userDao = userDao;
        this.squadInviteDao = squadInviteDao;
    }

    @GetMapping("/invites/sender")
    public Set<SquadInvite> getAllMySenderInvites() {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return squadInviteDao.findAllBySender(currentUser);
    }

    @GetMapping("/invites/recipient")
    public Set<SquadInvite> getAllMyRecipientInvites() {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return squadInviteDao.findAllByRecipient(currentUser);
    }

}

