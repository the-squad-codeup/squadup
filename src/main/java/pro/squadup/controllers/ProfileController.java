package pro.squadup.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pro.squadup.models.Preferences;
import pro.squadup.models.User;
import pro.squadup.repositories.LanguageRepository;
import pro.squadup.repositories.PlatformRepository;
import pro.squadup.repositories.PreferencesRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@Controller
public class ProfileController {

    private UserRepository userDao;
    private PreferencesRepository preferencesDao;
    private PlatformRepository platformDao;
    private LanguageRepository languageDao;

    public ProfileController(UserRepository userDao, PreferencesRepository preferencesDao, PlatformRepository platformDao, LanguageRepository languageDao) {
        this.userDao = userDao;
        this.preferencesDao = preferencesDao;
        this.platformDao = platformDao;
        this.languageDao = languageDao;
    }

    @GetMapping("/profile/preferences")
    public String preferencesPage(Model model){
        User user = userDao.findById(Utils.currentUserId()).get();
        if(user.getPreferences().getId() != null) {
            model.addAttribute("preferences", user.getPreferences());
        } else {
            Preferences preferences = new Preferences();
            preferencesDao.save(preferences);
            user.setPreferences(preferences);
            userDao.save(user);
            model.addAttribute("preferences", preferences);
        }
        model.addAttribute("languages", languageDao.findAll());
        model.addAttribute("platforms", platformDao.findAll());
        return "profile/preferences";
    }

    @GetMapping("/build-profile")
    public String buildProfilePage(){
        return "profile/build-profile";
    }

    @GetMapping("/games")
    public String gamesPage(){
        return "profile/games";
    }

    @PostMapping("/profile/preferences/{id}/edit")
    public String editProfilePreferences(@PathVariable Long id, @RequestBody Preferences updatedPreferences) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        System.out.println("Inside editProfile method");
        System.out.println(updatedPreferences.toString());
        Preferences userPreferences = currentUser.getPreferences();
        userPreferences.setBio(updatedPreferences.getBio());
        userPreferences.setLocation(updatedPreferences.getLocation());
        userPreferences.setLanguage(updatedPreferences.getLanguage());
        userPreferences.setMature_language(updatedPreferences.isMature_language());
        userPreferences.setGame_age_rating(updatedPreferences.getGame_age_rating());
        preferencesDao.save(userPreferences);
        return "redirect:/recruits";
    }


}
