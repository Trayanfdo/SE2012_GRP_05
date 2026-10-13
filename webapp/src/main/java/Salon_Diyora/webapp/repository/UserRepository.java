package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByPhoneAndPassword(String phone, String password);

    List<AppUser> findByRoleIn(List<String> roles);
}