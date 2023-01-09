package pro.squadup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.squadup.models.Squad;
import pro.squadup.repositories.SquadRepository;

@Controller
public class SquadController {

    private final SquadRepository squadDao;

    public SquadController(SquadRepository squadDao) {
        this.squadDao = squadDao;
    }

    @GetMapping("/squads")
    public String showSquadsPage(Model model) {
        model.addAttribute("squad", new Squad());
        return "squad/main";
    }
}
