package Salon_Diyora.webapp.model;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appointmentId;
    private int rating; // 1 to 5 stars
    private String feedback;

    // Remember to generate Getters and Setters here
}