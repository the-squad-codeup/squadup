package pro.squadup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PreferencesController {

    @GetMapping("/preferences")
    public String preferencesPage(){
        return "preferences/preferences";
    }

    @GetMapping("/build-profile")
    public String buildProfilePage(){
        return "preferences/build-profile";
    }

    @GetMapping("/games")
    public String gamesPage(){
        return "preferences/games";
    }

}
