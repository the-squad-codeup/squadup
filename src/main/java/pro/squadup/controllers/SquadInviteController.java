package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.squadup.models.Comrade;
import pro.squadup.models.Squad;
import pro.squadup.models.SquadInvite;
import pro.squadup.models.User;
import pro.squadup.repositories.SquadInviteRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.util.HashSet;
import java.util.Set;

@RestController
public class SquadInviteController {

    private final UserRepository userDao;
    private final SquadInviteRepository squadInviteDao;
    private final SquadRepository squadDao;

    public SquadInviteController(UserRepository userDao, SquadInviteRepository squadInviteDao, SquadRepository squadDao) {
        this.userDao = userDao;
        this.squadInviteDao = squadInviteDao;
        this.squadDao = squadDao;
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

    @GetMapping("/invites/{squadId}/possible")
    public Set<User> getAllPossibleUsersToInvite(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        Set<User> squadMembers = squad.getMembers();
        Set<User> possibleInvitees = new HashSet<>();
        for(User member : squadMembers) {
            Set<Comrade> comrades = member.getComrades();
            for(Comrade comrade : comrades) {
                User comradeUser = comrade.getUserTwo();
                if(!possibleInvitees.contains(comradeUser)) {
                    possibleInvitees.add(comradeUser);
                }
            }
        }
        return possibleInvitees;
    }

    @GetMapping("/invites/{squadId}/current")
    public Set<User> getAllCurrentInvitees(@PathVariable Long squadId) {
        Squad squad = squadDao.findById(squadId).get();
        Set<SquadInvite> invites = squadInviteDao.findAllBySquad(squad);
        Set<User> invitees = new HashSet<>();
        for(SquadInvite invite : invites) {
            invitees.add(invite.getRecipient());
        }
        return invitees;
    }

    @PostMapping("/invites/{squadId}/accept")
    public Squad acceptSquadInvite(@PathVariable Long squadId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Inside acceptSquadInvite");
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        System.out.printf("Current User: %n%s%n", mapper.writeValueAsString(currentUser));
        Squad squad = squadDao.findById(squadId).get();
        System.out.printf("Squad: %n%s%n", mapper.writeValueAsString(squad));
        if(squadInviteDao.existsBySquadAndRecipient(squad, currentUser)) {
            System.out.println("Squad invite exists by squad and recipient!");
            SquadInvite invite = squadInviteDao.findBySquadAndRecipient(squad, currentUser);
            System.out.printf("SquadInvite: %n%s%n", mapper.writeValueAsString(invite));
            Set<User> squadMembers = squad.getMembers();
            squadMembers.add(currentUser);
            squad.setMembers(squadMembers);
            System.out.printf("Updated squad: %n%s%n", mapper.writeValueAsString(squad));
            squadDao.save(squad);
            squadInviteDao.delete(invite);
        }
        return squad;
    }

    @PostMapping("/invites/{squadId}/reject")
    public void rejectSquadInvite(@PathVariable Long squadId) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Squad squad = squadDao.findById(squadId).get();
        if(squadInviteDao.existsBySquadAndRecipient(squad, currentUser)) {
            SquadInvite invite = squadInviteDao.findBySquadAndRecipient(squad, currentUser);
            squadInviteDao.delete(invite);
        }
    }
}
