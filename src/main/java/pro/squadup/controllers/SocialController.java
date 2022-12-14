package pro.squadup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pro.squadup.services.UrlService;


@Controller
public class SocialController {

    @Autowired
    private UrlService url;

    @GetMapping("/comrades")
    public String comradesPage(Model model){
        model.addAttribute("url", url);
        return "social/comrades";
    }

    @GetMapping("/recruits")
    public String recruitsPage(Model model){
        model.addAttribute("url", url);
        return "social/recruits";
    }

}
