package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Appointment;
import Salon_Diyora.webapp.model.Review;
import Salon_Diyora.webapp.repository.AppointmentRepository;
import Salon_Diyora.webapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, AppointmentRepository appointmentRepository) {
        this.reviewRepository = reviewRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Review submitReview(Review review) {
        // 1. Verify appointment exists
        Appointment app = appointmentRepository.findById(review.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        // 2. Validate that appointment is COMPLETED
        if (!"COMPLETED".equalsIgnoreCase(app.getStatus())) {
            throw new IllegalStateException("Feedback can only be submitted for completed appointments.");
        }

        // 3. Prevent duplicate feedback
        if (reviewRepository.existsByAppointmentId(review.getAppointmentId())) {
            throw new IllegalStateException("Feedback has already been submitted for this appointment.");
        }

        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }

        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public double calculateAverageRating() {
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) return 0.0;
        double sum = 0;
        for (Review r : reviews) sum += r.getRating();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}