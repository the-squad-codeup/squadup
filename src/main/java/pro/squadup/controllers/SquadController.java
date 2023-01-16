package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.*;
import pro.squadup.repositories.*;
import pro.squadup.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class SquadController {

    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final SquadPictureRepository squadPictureDao;
    private final SquadChatRepository squadChatDao;
    private final SquadChatMessageRepository squadChatMessageDao;
    private final SquadInviteRepository squadInviteDao;

    public SquadController(UserRepository userDao, SquadRepository squadDao, SquadPictureRepository squadPictureDao, SquadChatRepository squadChatDao, SquadChatMessageRepository squadChatMessageDao, SquadInviteRepository squadInviteDao) {
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.squadPictureDao = squadPictureDao;
        this.squadChatDao = squadChatDao;
        this.squadChatMessageDao = squadChatMessageDao;
        this.squadInviteDao = squadInviteDao;
    }

    @GetMapping("/squads")
    public String showSquadsPage(Model model) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Set<Squad> userSquads = squadDao.findAllByMembers(currentUser);
        model.addAttribute("userSquads", userSquads);
        model.addAttribute("squad", new Squad());
        return "squad/main";
    }

    @PostMapping("/squads/create")
    public String createSquad(Model model, @ModelAttribute Squad squad) throws JsonProcessingException {
        // getting user, default squadPicture, empty Chat to set in squad
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        SquadPicture squadPicture = Utils.defaultSquadPicture();
        SquadChat chat = new SquadChat();
        Set<User> members = new HashSet<>();
        // setting user, picture, chat
        squad.setOwner(currentUser);
        members.add(currentUser);
        squad.setMembers(members);
        // saving new entities
        squadPictureDao.save(squadPicture);
        squadChatDao.save(chat);
//        squadDao.save(squad);
        squad.setSquadPicture(squadPicture);
        squad.setChat(chat);
        // saving changes to squad
        squadDao.save(squad);
        model.addAttribute("squad", squad);
        return "squad/chat";
    }

    @PostMapping("/squads/create/new")
    public @ResponseBody Squad createNewSquad(@RequestBody NewSquadInfo newSquadInfo) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        SquadPicture squadPicture = squadPictureDao.findById(newSquadInfo.getSquadPictureId()).get();
        SquadChat chat = new SquadChat();
        Set<User> members = new HashSet<>();
        Squad squad = new Squad();
        squad.setOwner(currentUser);
        members.add(currentUser);
        squad.setMembers(members);
        squadChatDao.save(chat);
        squad.setSquadPicture(squadPicture);
        squad.setChat(chat);
        squad.setName(newSquadInfo.getName());
        squadDao.save(squad);
        for(Long inviteeId : newSquadInfo.getInviteeIds()) {
            SquadInvite invite = new SquadInvite(currentUser, userDao.findById(inviteeId).get(), squad);
            squadInviteDao.save(invite);
        }
        return squad;
    }

    @GetMapping("/squads/{squadId}/chat")
    public String showSquadPage(Model model, @PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        model.addAttribute("squad", squad);
        return "squad/chat";
    }

    @GetMapping("/squads/{squadId}/members")
    public @ResponseBody Set<User> getSquadMembers(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squad.getMembers();
    }

    @PostMapping("/squads/{squadId}/invite/{userId}")
    public @ResponseBody User inviteUser(@PathVariable Long squadId, @PathVariable Long userId) {
        Squad squad = squadDao.findById(squadId).get();
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        User invitee = userDao.findById(userId).get();
        SquadInvite invite = new SquadInvite();

        invite.setSquad(squad);
        invite.setSender(currentUser);
        invite.setRecipient(invitee);

        squadInviteDao.save(invite);

        return invitee;
    }

    @GetMapping("/squads/{squadId}/messages")
    public @ResponseBody Set<SquadChatMessage> getAllSquadChatMessages(@PathVariable Long squadId) {
        System.out.println("Inside getAllSquadChatMessages");
        Squad squad = squadDao.findById(squadId).get();
        return squadChatMessageDao.findAllByChat(squad.getChat());
    }

    @PostMapping("/squads/{squadId}/delete")
    public @ResponseBody Squad deleteSquad(@PathVariable Long squadId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squadToDelete = squadDao.findById(squadId).get();
        if(squadToDelete.getOwner().equals(currentUser)) {
            squadDao.delete(squadToDelete);
        }
        return squadToDelete;
    }

    @GetMapping("/squads/{squadId}/owner")
    public @ResponseBody User getSquadOwner(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squad.getOwner();
    }

}
