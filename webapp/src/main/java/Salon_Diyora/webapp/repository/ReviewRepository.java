package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Automatically generates SQL: SELECT * FROM reviews ORDER BY created_at DESC
    List<Review> findAllByOrderByCreatedAtDesc();

    // Automatically generates SQL: SELECT COUNT(*) > 0 FROM reviews WHERE appointment_id = ?
    boolean existsByAppointmentId(Long appointmentId);
}