package pro.squadup.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SocialController {

    @GetMapping("/comrades")
    public String comradesPage(){
        return "social/comrades";
    }

    @GetMapping("/recruits")
    public String recruitsPage(){
        return "social/recruits";
    }

}
