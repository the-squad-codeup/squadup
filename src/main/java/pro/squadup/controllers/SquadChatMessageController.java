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

    @PostMapping("/messages/{messageId}/edit")
    public SquadChatMessage editMessage(@PathVariable Long messageId, @RequestBody SquadChatMessage message) {
        SquadChatMessage messageToEdit = squadChatMessageDao.findById(messageId).get();
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        if(message.getSender().equals(currentUser) && messageToEdit.getSender().equals(currentUser) && message.getId() == messageToEdit.getId()) {
            messageToEdit.setContent(message.getContent());
            messageToEdit.setEdited(true);
            squadChatMessageDao.save(messageToEdit);
        }
        return messageToEdit;
    }

    @PostMapping("/messages/{messageId}/delete")
    public SquadChatMessage deleteMessage(@PathVariable Long messageId) {
        SquadChatMessage messageToDelete = squadChatMessageDao.findById(messageId).get();
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        if(messageToDelete.getSender().equals(currentUser)) {
            squadChatMessageDao.delete(messageToDelete);
        }
        return messageToDelete;
    }
}
