package pro.squadup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.LastSeenMessage;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadChatMessage;
import pro.squadup.models.User;
import pro.squadup.repositories.LastSeenMessageRepository;
import pro.squadup.repositories.SquadChatMessageRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@RestController
public class LastSeenMessageController {

    private final LastSeenMessageRepository lastSeenMessageDao;
    private final SquadRepository squadDao;
    private final UserRepository userDao;
    private final SquadChatMessageRepository squadChatMessageDao;

    public LastSeenMessageController(LastSeenMessageRepository lastSeenMessageDao, SquadRepository squadDao, UserRepository userDao, SquadChatMessageRepository squadChatMessageDao) {
        this.lastSeenMessageDao = lastSeenMessageDao;
        this.squadDao = squadDao;
        this.userDao = userDao;
        this.squadChatMessageDao = squadChatMessageDao;
    }

    // returns last seen message object from current user and squad id path variable
    @GetMapping("messages/last/{squadId}")
    public LastSeenMessage getLastSeenMessage(@PathVariable Long squadId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        if(lastSeenMessageDao.existsByUserAndSquad(currentUser, squad)) {
            return lastSeenMessageDao.findByUserAndSquad(currentUser, squad);
        }
        return new LastSeenMessage();
    }

    // sets last seen message object from current user, squad id, and squad chat message id
    @PostMapping("messages/last/{squadId}/{messageId}")
    public LastSeenMessage setLastSeenMessage(@PathVariable Long squadId, @PathVariable Long messageId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        SquadChatMessage message = squadChatMessageDao.findById(messageId).get();
        if(!lastSeenMessageDao.existsByUserAndSquad(currentUser, squad)) {
            LastSeenMessage lastSeenMessage = new LastSeenMessage(currentUser, squad, message);
            lastSeenMessageDao.save(lastSeenMessage);
            return lastSeenMessage;
        }
        LastSeenMessage lastSeenMessage = lastSeenMessageDao.findByUserAndSquad(currentUser, squad);
        lastSeenMessage.setSquadChatMessage(message);
        lastSeenMessageDao.save(lastSeenMessage);
        return lastSeenMessage;
    }
}
