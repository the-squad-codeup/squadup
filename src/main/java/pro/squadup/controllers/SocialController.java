package pro.squadup.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SocialController {

    // simple getmapping redirects

    @GetMapping("/hq")
    public String socialHQPage() {
        return "social/social-hq";
    }

}
