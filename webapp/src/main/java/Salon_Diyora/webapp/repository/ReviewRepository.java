package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Custom query method to get latest reviews first
    List<Review> findAllByOrderByCreatedAtDesc();

    // Custom query to filter reviews by rating
    List<Review> findByRatingGreaterThanEqual(int rating);
}