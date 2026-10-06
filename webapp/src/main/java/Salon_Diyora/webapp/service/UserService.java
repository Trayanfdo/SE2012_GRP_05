package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.AppUser;
import Salon_Diyora.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser registerUser(AppUser user) {
        // Default new registrations to CUSTOMER if not specified
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        }
        return userRepository.save(user);
    }

    public AppUser loginUser(String phone, String password) {
        Optional<AppUser> user = userRepository.findByPhoneAndPassword(phone, password);
        return user.orElse(null); // Returns null if login fails
    }

    public List<AppUser> getStaffProfiles() {

        return userRepository.findByRole("STYLIST");
    }
}