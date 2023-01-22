package pro.squadup.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.*;
import pro.squadup.repositories.*;
import pro.squadup.utils.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ProfileController {

    private ObjectMapper mapper = new ObjectMapper();

    private UserRepository userDao;
    private PreferencesRepository preferencesDao;
    private PlatformRepository platformDao;
    private LanguageRepository languageDao;
    private LocationRepository locationDao;
    private RatingRepository ratingDao;
    private ComradeRepository comradeDao;
    private RecruitRepository recruitDao;

    public ProfileController(
            UserRepository userDao,
            PreferencesRepository preferencesDao,
            PlatformRepository platformDao,
            LanguageRepository languageDao,
            LocationRepository locationDao,
            RatingRepository ratingDao,
            ComradeRepository comradeDao,
            RecruitRepository recruitDao
    ) {
        this.userDao = userDao;
        this.preferencesDao = preferencesDao;
        this.platformDao = platformDao;
        this.languageDao = languageDao;
        this.locationDao = locationDao;
        this.ratingDao = ratingDao;
        this.comradeDao = comradeDao;
        this.recruitDao = recruitDao;
    }

    @GetMapping("/profile")
    public String myProfilePage(Model model) {
        User user = userDao.findById(Utils.currentUserId()).get();
        prepProfileRedirect(model, user);
        return "profile/profile";
    }

    @GetMapping("/profile/{comradeId}/comrade")
    public String profilePageFromComrade(Model model, @PathVariable Long comradeId) {
        User user = userDao.findById(comradeDao.findById(comradeId).get().getUserTwo().getId()).get();
        prepProfileRedirect(model, user);
        return "profile/profile";
    }

    @GetMapping("/profile/{recruitId}/recruit")
    public String profilePageFromRecruit(Model model, @PathVariable Long recruitId) {
        User user = userDao.findById(recruitDao.findById(recruitId).get().getUserTwo().getId()).get();
        prepProfileRedirect(model, user);
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

    @PostMapping("/profile/preferences/edit")
    public @ResponseBody String editProfilePreferences(@RequestBody Preferences updatedPreferences, Model model) throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        Preferences userPreferences = currentUser.getPreferences();
        packagePreferences(userPreferences, updatedPreferences);
        preferencesDao.save(userPreferences);
        return "redirect:/hq";
    }


    private boolean isMyProfile(User currentUser, User user) {
        return currentUser.getId().equals(user.getId());
    }

    private boolean isRecruit(User currentUser, User user) {
        for(Recruit recruit : currentUser.getRecruits()) {
            if(recruit.getUserTwo().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isComrade(User currentUser, User user) {
        for(Comrade comrade : currentUser.getComrades()) {
            if(comrade.getUserTwo().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    private void prepProfileRedirect(Model model, User user) {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        if(isMyProfile(currentUser, user)) {
            model.addAttribute("user", currentUser);
        } else {
            model.addAttribute("user", user);
        }
        model.addAttribute("isMyProfile", isMyProfile(currentUser, user));
        model.addAttribute("isRecruit", isRecruit(currentUser, user));
        model.addAttribute("isComrade", isComrade(currentUser, user));
    }

    private void packagePreferences(Preferences userPreferences, Preferences updatedPreferences) {
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
    }
}
