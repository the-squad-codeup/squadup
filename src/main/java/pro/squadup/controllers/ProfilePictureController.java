package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.ProfilePicture;
import pro.squadup.models.User;
import pro.squadup.repositories.ProfilePictureRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

@RestController
public class ProfilePictureController {

    private final UserRepository userDao;

    private final ProfilePictureRepository profilePictureDao;

    public ProfilePictureController(UserRepository userDao, ProfilePictureRepository profilePictureDao) {
        this.userDao = userDao;
        this.profilePictureDao = profilePictureDao;
    }

    @GetMapping("/user/picture")
    public ProfilePicture getMyProfilePicture() throws JsonProcessingException {
        if(Utils.currentUserId() == null) {
            return new ProfilePicture(-1L, "https://cdn.filestackcontent.com/dynXYf9AS26ImuqTgUPj");
        } else {
            return profilePictureDao.findByUser(userDao.findById(Utils.currentUserId()).get());
        }
    }

    @PostMapping("/user/picture")
    public ProfilePicture setProfilePicture(@RequestBody ProfilePicture picture) throws JsonProcessingException {
        User user = userDao.findById(Utils.currentUserId()).get();
        ProfilePicture currentPicture = profilePictureDao.findByUser(user);
        if(currentPicture != null) {
            profilePictureDao.delete(currentPicture);
        }
        picture.setUser(user);
        profilePictureDao.save(picture);
        return picture;
    }

    @GetMapping("/user/{userId}/picture")
    public ProfilePicture getProfilePicture(@PathVariable Long userId) {
        User user = userDao.findById(userId).get();
        if(profilePictureDao.existsByUser(user)) {
            return profilePictureDao.findByUser(user);
        }
        return new ProfilePicture(1L, "https://cdn.filestackcontent.com/dynXYf9AS26ImuqTgUPj");
    }
}
