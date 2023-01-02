package pro.squadup.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pro.squadup.models.Language;
import pro.squadup.models.Platform;
import pro.squadup.models.Preferences;
import pro.squadup.models.User;
import pro.squadup.repositories.*;
import pro.squadup.services.UrlService;
import pro.squadup.utils.Utils;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ProfileController {

    private UserRepository userDao;
    private PreferencesRepository preferencesDao;
    private PlatformRepository platformDao;
    private LanguageRepository languageDao;
    private LocationRepository locationDao;
    private RatingRepository ratingDao;

    @Autowired
    private UrlService url;

    public ProfileController(
            UserRepository userDao,
            PreferencesRepository preferencesDao,
            PlatformRepository platformDao,
            LanguageRepository languageDao,
            LocationRepository locationDao,
            RatingRepository ratingDao
    ) {
        this.userDao = userDao;
        this.preferencesDao = preferencesDao;
        this.platformDao = platformDao;
        this.languageDao = languageDao;
        this.locationDao = locationDao;
        this.ratingDao = ratingDao;
    }

    @GetMapping("/profile")
    public String myProfilePage(Model model) {
        User user = userDao.findById(Utils.currentUserId()).get();
        model.addAttribute("user", user);
        model.addAttribute("url", url);
        return "profile/profile";
    }

    @GetMapping("/profile/preferences")
    public String preferencesPage(Model model){
        User user = userDao.findById(Utils.currentUserId()).get();
        if(user.getPreferences() != null) {
            model.addAttribute("preferences", user.getPreferences());
        } else {
            Preferences preferences = new Preferences();
            preferencesDao.save(preferences);
            user.setPreferences(preferences);
            userDao.save(user);
            model.addAttribute("preferences", preferences);
        }
        model.addAttribute("url", url);
        model.addAttribute("languages", languageDao.findAll());
        model.addAttribute("platforms", platformDao.findAll());
        return "profile/preferences";
    }

    @GetMapping("/build-profile")
    public String buildProfilePage(Model model){
        model.addAttribute("url", url);
        return "profile/build-profile";
    }

    @GetMapping("/games")
    public String gamesPage(Model model){
        model.addAttribute("url", url);
        return "profile/games";
    }

    @PostMapping("/profile/preferences/{id}/edit")
    public String editProfilePreferences(@PathVariable Long id, @RequestBody Preferences updatedPreferences) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Preferences userPreferences = currentUser.getPreferences();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(userPreferences));
        userPreferences.setBio(updatedPreferences.getBio());
        userPreferences.setLocation(locationDao.findByTimezone(updatedPreferences.getLocation().getTimezone()));
        Set<Language> updatedLanguages = new HashSet<>();
        for(Language language : updatedPreferences.getLanguages()) {
            updatedLanguages.add(languageDao.findByLanguage(language.getLanguage()));
        }
        userPreferences.setLanguages(updatedLanguages);
        userPreferences.setMatureLanguage(updatedPreferences.isMatureLanguage());
        userPreferences.setRating(ratingDao.findByRating(updatedPreferences.getRating().getRating()));
        Set<Platform> updatedPlatforms = new HashSet<>();
        for(Platform platform : updatedPreferences.getPlatforms()) {
            updatedPlatforms.add(platformDao.findByType(platform.getType()));
        }
        userPreferences.setPlatforms(updatedPlatforms);
        userPreferences.setGamertag(updatedPreferences.getGamertag());
        preferencesDao.save(userPreferences);
        return "redirect:/recruits";
    }


}
