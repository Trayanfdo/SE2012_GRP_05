package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Spring Boot automatically translates this exact method name into a SQL sorting query
    List<Review> findAllByOrderByCreatedAtDesc();

}