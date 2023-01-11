package pro.squadup.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.SquadChatMessage;
import pro.squadup.models.User;
import pro.squadup.repositories.SquadChatMessageRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@RestController
public class SquadChatMessageController {

    private final UserRepository userDao;
    private final SquadChatMessageRepository squadChatMessageDao;

    public SquadChatMessageController(UserRepository userDao, SquadChatMessageRepository squadChatMessageDao) {
        this.userDao = userDao;
        this.squadChatMessageDao = squadChatMessageDao;
    }
}
