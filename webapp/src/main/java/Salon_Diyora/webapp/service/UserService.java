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
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("CUSTOMER");
        } else {
            user.setRole(user.getRole().trim().toUpperCase());
        }
        return userRepository.save(user);
    }

    public AppUser loginUser(String phone, String password) {
        Optional<AppUser> user = userRepository.findByPhoneAndPassword(phone, password);
        return user.orElse(null);
    }

    public List<AppUser> getStaffProfiles() {
        return userRepository.findByRoleIn(List.of("STYLIST", "STYLIST (STAFF)", "STAFF"));
    }

    public List<AppUser> getStylistsOnly() {
        return userRepository.findByRoleIn(List.of("STYLIST", "STYLIST (STAFF)"));
    }

    public AppUser updateUser(Long id, AppUser updatedUser) {
        return userRepository.findById(id).map(existing -> {
            existing.setName(updatedUser.getName());
            existing.setPhone(updatedUser.getPhone());
            if (updatedUser.getRole() != null) {
                existing.setRole(updatedUser.getRole().trim().toUpperCase());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existing.setPassword(updatedUser.getPassword());
            }
            return userRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}