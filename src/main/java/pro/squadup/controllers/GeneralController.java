package pro.squadup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pro.squadup.models.*;
import pro.squadup.repositories.*;
import pro.squadup.utils.Utils;

import java.util.Set;

@Controller
public class GeneralController {

    private final StrayRepository strayDao;
    private final UserRepository userDao;
    private final SquadRepository squadDao;
    private final ComradeRepository comradeDao;
    private final RecruitRepository recruitDao;

    public GeneralController(StrayRepository strayDao, UserRepository userDao, SquadRepository squadDao, ComradeRepository comradeDao, RecruitRepository recruiteDao) {
        this.strayDao = strayDao;
        this.userDao = userDao;
        this.squadDao = squadDao;
        this.comradeDao = comradeDao;
        this.recruitDao = recruiteDao;
    }

    // simple get mapping redirects
    @GetMapping("/")
    public String showSplashPage() {
        return "general/splash";
    }

    @GetMapping("/about")
    public String showAboutUsPage() {
        return "general/about-us";
    }

    @GetMapping("/contact")
    public String contactUsEmail() {
        return "general/contact-us";
    }

    // saves contact us form data into database strays table
    @PostMapping("/contact")
    public String submittedEmail(@ModelAttribute Stray stray){
        strayDao.save(stray);
        return "redirect:/";
    }

    // redirects to dashboard with current user's squads passed in
    @GetMapping("/dashboard")
    public String showHomePage(Model model) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Set<Squad> squads = squadDao.findAllByMembers(currentUser);
        model.addAttribute("userSquads", squads);
        return "general/dashboard";
    }
}
