package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.AppUser;
import Salon_Diyora.webapp.repository.UserRepository; // You must create this interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) {
        return userRepository.save(user);
    }

    @GetMapping("/staff")
    public List<AppUser> getStaff() {
        // Advanced tip: Later, update UserRepository to filter by role="STYLIST"
        return userRepository.findAll();
    }
}