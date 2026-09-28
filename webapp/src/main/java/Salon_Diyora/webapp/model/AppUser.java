package Salon_Diyora.webapp.model;

import jakarta.persistence.*;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone; // Used for bookings since customers currently call in
    private String role; // "CUSTOMER", "STYLIST", "OWNER"

    // Remember to generate Getters and Setters here
}