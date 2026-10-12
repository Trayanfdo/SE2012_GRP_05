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
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        }
        return userRepository.save(user);
    }

    public AppUser loginUser(String phone, String password) {
        Optional<AppUser> user = userRepository.findByPhoneAndPassword(phone, password);
        return user.orElse(null);
    }

    public List<AppUser> getStaffProfiles() {
        return userRepository.findByRoleIn(List.of("STYLIST", "RECEPTIONIST"));
    }

    public List<AppUser> getStylistsOnly() {
        return userRepository.findByRoleIn(List.of("STYLIST"));
    }

    public AppUser updateUser(Long id, AppUser updated) {
        return userRepository.findById(id).map(user -> {
            user.setName(updated.getName());
            user.setPhone(updated.getPhone());
            user.setRole(updated.getRole());
            return userRepository.save(user);
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