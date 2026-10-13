package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Fetch newest reviews first for the Owner Dashboard
    List<Review> findAllByOrderByCreatedAtDesc();

    // Check if feedback was already submitted for an appointment
    boolean existsByAppointmentId(Long appointmentId);
}