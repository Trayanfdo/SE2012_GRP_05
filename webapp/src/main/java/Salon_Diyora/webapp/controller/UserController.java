package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.AppUser;
import Salon_Diyora.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody AppUser credentials) {
        AppUser loggedInUser = userService.loginUser(credentials.getPhone(), credentials.getPassword());
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(401).body("Invalid phone number or password.");
        }
    }

    @GetMapping("/staff")
    public List<AppUser> getStaff() {
        return userService.getStaffProfiles();
    }

    @GetMapping("/stylists")
    public List<AppUser> getStylistsOnly() {
        return userService.getStylistsOnly();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser updated) {
        AppUser res = userService.updateUser(id, updated);
        return res != null ? ResponseEntity.ok(res) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok("User deleted successfully.") : ResponseEntity.notFound().build();
    }
}