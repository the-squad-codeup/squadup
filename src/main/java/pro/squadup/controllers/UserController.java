package pro.squadup.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pro.squadup.models.ProfilePicture;
import pro.squadup.models.Squad;
import pro.squadup.models.User;
import pro.squadup.models.UserSlim;
import pro.squadup.repositories.ProfilePictureRepository;
import pro.squadup.repositories.SquadRepository;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    // Repositories and Services
    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePictureRepository profilePictureDao;
    private final SquadRepository squadDao;

    // Constructor
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, ProfilePictureRepository profilePictureDao, SquadRepository squadDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.profilePictureDao = profilePictureDao;
        this.squadDao = squadDao;
    }

    // Shows form to sign up as a user
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        // Sending empty user to template
        model.addAttribute("user", new User());
        return "user/signup";
    }

    // Saves user to users table
    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user, HttpServletRequest httpServletRequest) {
        String plainPassword = user.getPassword();
        // Hashing password
        String hash = passwordEncoder.encode(user.getPassword());
        // Setting user password to the hash and saving user to table
        user.setPassword(hash);
        ProfilePicture defaultProfilePicture = new ProfilePicture
                (
                    "default.png",
                    "dynXYf9AS26ImuqTgUPj",
                    "image/png",
                    47871,
                    "local_file_system",
                    "Stored",
                    "4IRT98MxlthTjGsm",
                    "https://cdn.filestackcontent.com/dynXYf9AS26ImuqTgUPj"
                )
        ;
        userDao.save(user);
        defaultProfilePicture.setUser(user);
        profilePictureDao.save(defaultProfilePicture);
        authWithHttpServletRequest(httpServletRequest, user.getUsername(), plainPassword);
        return "redirect:/profile/preferences";
    }

    // returns current user
    @GetMapping("/user/get")
    public @ResponseBody User getCurrentUser() {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return currentUser;
    }

    @GetMapping("/user/all")
    public @ResponseBody List<UserSlim> getAllUsers() {
        List<User> allUsers = userDao.findAll();
        List<UserSlim> allSlimUsers = new ArrayList<>();
        for(User user : allUsers) {
            allSlimUsers.add(new UserSlim(user.getUsername(), user.getEmail()));
        }
        return allSlimUsers;
    }

    // returns user based on user id
    @GetMapping("/user/{userId}/info")
    public @ResponseBody User getUserInfo(@PathVariable Long userId) {
        return userDao.findById(userId).get();
    }

    // returns all squads current user is member of
    @GetMapping("/user/squads")
    public @ResponseBody Set<Squad> getAllSquads() {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return squadDao.findAllByMembers(currentUser);
    }

    // returns all squads current user is owner of
    @GetMapping("/user/squads-owned")
    public @ResponseBody Set<Squad> getAllSquadsOwned() {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        return squadDao.findAllByOwner(currentUser);
    }

    // logs user in
    private void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}
