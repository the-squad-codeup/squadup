package pro.squadup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pro.squadup.models.Stray;
import pro.squadup.repositories.StrayRepository;

@Controller
public class GeneralController {

    private final StrayRepository strayDao;

    public GeneralController(StrayRepository strayDao) {
        this.strayDao = strayDao;
    }

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

    @PostMapping("/contact")
    public String submittedEmail(@ModelAttribute Stray stray){
        strayDao.save(stray);
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String showHomePage() {
        return "general/dashboard";
    }
}
