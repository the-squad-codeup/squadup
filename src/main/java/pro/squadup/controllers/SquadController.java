package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.*;
import pro.squadup.repositories.*;
import pro.squadup.utils.Utils;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

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

    // returns squad object based on squad id passed in
    @GetMapping("/squads/{squadId}/info")
    public @ResponseBody Squad getSquadInfoById(@PathVariable Long squadId) {
        return squadDao.findById(squadId).get();
    }

    // creates squad based on current user and squad object passed in
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
        squad.setSquadPicture(squadPicture);
        squad.setChat(chat);
        // saving changes to squad
        squadDao.save(squad);
        model.addAttribute("squad", squad);
        return "squad/chat";
    }

    // creates new squad based on new squad info passed in
    // uses a custom NewSquadInfo object that has limited squad information
    @PostMapping("/squads/create/new")
    public @ResponseBody Squad createNewSquad(@RequestBody NewSquadInfo newSquadInfo) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        SquadPicture squadPicture;
        if(newSquadInfo.getSquadPictureId() != null) {
            squadPicture = squadPictureDao.findById(newSquadInfo.getSquadPictureId()).get();
        } else {
            squadPicture = Utils.defaultSquadPicture();
        }
        SquadChat chat = new SquadChat();
        Set<User> members = new HashSet<>();
        Squad squad = new Squad();
        squad.setOwner(currentUser);
        members.add(currentUser);
        squad.setMembers(members);
        squadChatDao.save(chat);
        squad.setSquadPicture(squadPicture);
        squad.setChat(chat);
        if(!newSquadInfo.getName().equals("")) {
            squad.setName(newSquadInfo.getName());
        } else {
            squad.setName(format("%s's Squad", currentUser.getUsername()));
        }
        squadDao.save(squad);
        if(newSquadInfo.getInviteeIds() != null) {
            for (Long inviteeId : newSquadInfo.getInviteeIds()) {
                SquadInvite invite = new SquadInvite(currentUser, userDao.findById(inviteeId).get(), squad);
                squadInviteDao.save(invite);
            }
        }
        return squad;
    }

    // redirects to squad chat page based on squad id in path
    @GetMapping("/squads/{squadId}/chat")
    public String showSquadPage(Model model, @PathVariable Long squadId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        if(squad.getMembers().contains(currentUser)) {
            model.addAttribute("squad", squad);
            return "squad/chat";
        }
        return "social/social-hq";
    }

    // returns members of squad based on squad id in path
    @GetMapping("/squads/{squadId}/members")
    public @ResponseBody Set<User> getSquadMembers(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squad.getMembers();
    }

    // invites user to squad based on squad id and user id in path
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

    // returns set of all chat messages based on squad id
    @GetMapping("/squads/{squadId}/messages")
    public @ResponseBody Set<SquadChatMessage> getAllSquadChatMessages(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squadChatMessageDao.findAllByChat(squad.getChat());
    }

    // deletes squad based on squad id
    // only happens if current user is owner of squad
    @PostMapping("/squads/{squadId}/delete")
    public @ResponseBody Squad deleteSquad(@PathVariable Long squadId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squadToDelete = squadDao.findById(squadId).get();
        for(SquadInvite invite : squadInviteDao.findAllBySquad(squadToDelete)) {
            squadInviteDao.delete(invite);
        }
        if(squadToDelete.getOwner().equals(currentUser)) {
            squadDao.delete(squadToDelete);
        }
        return squadToDelete;
    }

    // returns user who is owner of squad based on squad id
    @GetMapping("/squads/{squadId}/owner")
    public @ResponseBody User getSquadOwner(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        return squad.getOwner();
    }

}
