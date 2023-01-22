package pro.squadup.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SocialController {

    // simple getmapping redirects
    @GetMapping("/comrades")
    public String comradesPage(){
        return "social/comrades";
    }

    @GetMapping("/recruits")
    public String recruitsPage(){
        return "social/recruits";
    }

    @GetMapping("/hq")
    public String socialHQPage() {
        return "social/social-hq";
    }

}
