package pro.squadup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.squadup.models.User;
import pro.squadup.services.UrlService;

@Controller
public class AuthenticationController {

    @Autowired
    private UrlService url;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("url", url);
        model.addAttribute("user", new User());
        return "user/login";
    }
}
