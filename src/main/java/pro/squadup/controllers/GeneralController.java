package pro.squadup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/")
    public String showSplashPage() {
        return "general/splash";
    }

    @GetMapping("/about")
    public String showAboutUsPage() {
        return "general/about-us";
    }

    @GetMapping("/contact")
    public String showContactUsPage() {
        return "general/contact-us";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "general/home";
    }
}
