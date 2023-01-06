package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("inside setProfilePicture. picture to add: ");
        System.out.println(mapper.writeValueAsString(picture));
        User user = userDao.findById(Utils.currentUserId()).get();
        System.out.println("Current user:");
        System.out.println(mapper.writeValueAsString(user));
        ProfilePicture currentPicture = profilePictureDao.findByUser(user);
        System.out.println("Current user's profile picture");
        System.out.println(mapper.writeValueAsString(currentPicture));
//        picture.setUser(user);
//        userDao.save(user);
//        profilePictureDao.save(picture);
//        if(currentPicture != null) {
//            System.out.println("Inside conditional, currentPicture is not null. Deleting currentPicture");
//            profilePictureDao.deleteById(currentPicture.getId());
//        }
        if(currentPicture != null) {
            System.out.println("Inside conditional, currentPicture is not null. Deleting currentPicture");
            profilePictureDao.deleteById(currentPicture.getId());
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
