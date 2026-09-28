package Salon_Diyora.webapp.model;

import jakarta.persistence.*;

@Entity
public class SalonServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName; // e.g., "Spa Treatment"
    private double price;
    private int durationMinutes;

    // Remember to generate Getters and Setters here
}