package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pro.squadup.models.*;
import pro.squadup.repositories.SquadChatRepository;
import pro.squadup.repositories.SquadPictureRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@Controller
public class SquadController {

    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final SquadPictureRepository squadPictureDao;
    private final SquadChatRepository squadChatDao;

    public SquadController(UserRepository userDao, SquadRepository squadDao, SquadPictureRepository squadPictureDao, SquadChatRepository squadChatDao) {
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.squadPictureDao = squadPictureDao;
        this.squadChatDao = squadChatDao;
    }

    @GetMapping("/squads")
    public String showSquadsPage(Model model) {
        model.addAttribute("squad", new Squad());
        return "squad/main";
    }

    @PostMapping("/squads/create")
    public String createSquad(Model model, @ModelAttribute Squad squad) throws JsonProcessingException {
        // saving squad to give it an id in database
        squadDao.save(squad);

        // getting user, default squadPicture, empty Chat to set in squad
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        SquadPicture squadPicture = Utils.defaultSquadPicture();
        SquadChat chat = new SquadChat();

        // setting user, picture, chat
        squad.setOwner(currentUser);
        squad.setSquadPicture(squadPicture);
        squad.setChat(chat);

        // saving new entities
        squadPictureDao.save(squadPicture);
        squadChatDao.save(chat);

        // saving changes to squad
        squadDao.save(squad);

        model.addAttribute("squad", squad);

        ObjectMapper mapper = new ObjectMapper();
        System.out.printf("Squad created: %n%s%n", mapper.writeValueAsString(squad));
        return "squad/chat";
    }
}
