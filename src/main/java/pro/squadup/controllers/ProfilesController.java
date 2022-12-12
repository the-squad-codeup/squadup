package pro.squadup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilesController {

    @GetMapping("/profile")
    public String profilePage(){
        return "profile/profile";
    }

    @GetMapping("/build-profile")
    public String buildProfilePage(){
        return "profile/build-profile";
    }

    @GetMapping("/games")
    public String gamesPage(){
        return "profile/games";
    }

}
