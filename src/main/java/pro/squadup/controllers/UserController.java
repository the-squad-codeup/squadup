package pro.squadup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.squadup.models.ProfilePicture;
import pro.squadup.models.User;
import pro.squadup.repositories.UserRepository;
import pro.squadup.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    // Repositories and Services
    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    // Constructor
    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
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
        user.setProfilePicture(new ProfilePicture(
                "default.png",
                "dynXYf9AS26ImuqTgUPj",
                "image/png",
                47871,
                "local_file_system",
                "Stored",
                "4IRT98MxlthTjGsm",
                "https://cdn.filestackcontent.com/dynXYf9AS26ImuqTgUPj"
        ));
        userDao.save(user);
        authWithHttpServletRequest(httpServletRequest, user.getUsername(), plainPassword);
        return "redirect:/profile/preferences";
    }

    @GetMapping("/user/get")
    public @ResponseBody User getCurrentUser() throws JsonProcessingException {
        User currentUser = userDao.findById(Utils.currentUserId()).get();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(currentUser);
        return currentUser;
    }

    private void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}
