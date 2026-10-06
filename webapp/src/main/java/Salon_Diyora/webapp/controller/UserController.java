package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.AppUser;
import Salon_Diyora.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser credentials) {
        AppUser loggedInUser = userService.loginUser(credentials.getPhone(), credentials.getPassword());
        if (loggedInUser != null) {
            return "Login Successful. Welcome " + loggedInUser.getName() + " (" + loggedInUser.getRole() + ")";
        } else {
            return "Invalid phone number or password.";
        }
    }

    @GetMapping("/staff")
    public List<AppUser> getStaff() {
        return userService.getStaffProfiles();
    }
}