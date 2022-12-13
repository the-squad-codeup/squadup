package pro.squadup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pro.squadup.models.Profile;
import pro.squadup.models.User;
import pro.squadup.repositories.LanguageRepository;
import pro.squadup.repositories.PlatformRepository;
import pro.squadup.repositories.ProfilesRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import java.util.prefs.Preferences;

@Controller
public class ProfilesController {

    private UserRepository userDao;
    private ProfilesRepository profileDao;
    private PlatformRepository platformDao;
    private LanguageRepository languageDao;

    public ProfilesController(UserRepository userDao, ProfilesRepository profileDao, PlatformRepository platformDao, LanguageRepository languageDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.platformDao = platformDao;
        this.languageDao = languageDao;
    }

    @GetMapping("/profile")
    public String profilePage(Model model){
        User user = userDao.findById(Utils.currentUserId()).get();
        if(user.getProfile().getId() != null) {
            model.addAttribute("preferences", user.getProfile());
        } else {
            Profile profile = new Profile();
            profileDao.save(profile);
            user.setProfile(profile);
            userDao.save(user);
            model.addAttribute("preferences", profile);
        }
        model.addAttribute("languages", languageDao.findAll());
        model.addAttribute("platforms", platformDao.findAll());
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

    @PostMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable Long id, @RequestBody Profile updatedPreferences) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        System.out.println("Inside editProfile method");
        System.out.println(updatedPreferences.toString());
        Profile userPreferences = currentUser.getProfile();
        userPreferences.setBio(updatedPreferences.getBio());
        userPreferences.setLocation(updatedPreferences.getLocation());
        userPreferences.setLanguage(updatedPreferences.getLanguage());
        userPreferences.setMature_language(updatedPreferences.isMature_language());
        userPreferences.setGame_age_rating(updatedPreferences.getGame_age_rating());
        profileDao.save(userPreferences);
        return "redirect:/recruits";
    }


}
