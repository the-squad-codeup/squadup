package pro.squadup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadChatMessage;
import pro.squadup.models.User;
import pro.squadup.repositories.SquadChatMessageRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.sql.Timestamp;
import java.util.Date;

import static java.lang.String.format;

@Controller
public class SquadChatController {

    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final SquadChatMessageRepository squadChatDao;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public SquadChatController(UserRepository userDao, SquadRepository squadDao, SquadChatMessageRepository squadChatDao) {
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.squadChatDao = squadChatDao;
    }

    @MessageMapping("/squad-chat/{squadId}/add-user")
    public void addUser(@DestinationVariable Long squadId, @Payload SquadChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        initializeMessage(chatMessage, squadId);
        squadChatDao.save(chatMessage);
        Long currentSquadId = (Long) headerAccessor.getSessionAttributes().put("squad_id", squadId);
        if(currentSquadId != null) {
            SquadChatMessage leaveMessage = initializeMessage(new SquadChatMessage(), squadId);
            squadChatDao.save(leaveMessage);
            messagingTemplate.convertAndSend(format("/secured/squad-room/%s", currentSquadId), leaveMessage);
        }
        headerAccessor.getSessionAttributes().put("name", chatMessage.getSender());
        messagingTemplate.convertAndSend(format("/secured/squad-room/%s", squadId), chatMessage);
    }

    @MessageMapping("/squad-chat/{squadId}/remove-user")
    public void removeUser(@DestinationVariable Long squadId, @Payload SquadChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        initializeMessage(chatMessage, squadId);
    }

    @MessageMapping("/squad-chat/{squadId}/send")
    public void sendMessage(@DestinationVariable Long squadId, @Payload SquadChatMessage chatMessage) {
        initializeMessage(chatMessage, squadId);
        squadChatDao.save(chatMessage);
        messagingTemplate.convertAndSend(format("/secured/squad-room/%s", squadId), chatMessage);
    }

    private SquadChatMessage initializeMessage(SquadChatMessage message, Long squadId) {
        Timestamp messageTime = new Timestamp(new Date().getTime());
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        message.setSender(currentUser);
        message.setTimestamp(messageTime);
        message.setChat(squad.getChat());
        return message;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////// Unsure if needed ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public OutputMessage send(Message message) throws Exception {
//        Timestamp time = new Timestamp(new Date().getTime());
//        return new OutputMessage(message.getSender(), message.getText(), time);
//    }

}
