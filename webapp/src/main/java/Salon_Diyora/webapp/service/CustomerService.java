package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Customer;
import Salon_Diyora.webapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        To build your Booking & Management System for Salon Diyora, you must structure your Spring Boot project using the MVC (Model-View-Controller) pattern. Based on the file structure in your image (`image_a32335.png`), you should create three new folders (packages) inside `src/main/java/Salon_Diyora/webapp`:
*   `model`: For your database tables (Entities).
                *   `repository`: For database operations (saving, fetching data).
                *   `controller`: For your RESTful APIs that connect to the front-end Web application.

        Before pasting the code, ensure your `pom.xml` includes dependencies for `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, and a database driver (like `h2` or `mysql-connector-j`).

***

### 1. Appointment & Scheduling (IT25101264)
                **Reasoning:** You are responsible for the core booking API and slot-availability logic to prevent double bookings. Your API receives booking requests from the UI and saves them to the database.

**Entity (`src/main/java/Salon_Diyora/webapp/model/Appointment.java`)**
```java
package Salon_Diyora.webapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

        @Entity
        public class Appointment {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            private Long customerId;
            private Long stylistId;
            private LocalDateTime appointmentTime;
            private String status; // e.g., "PENDING", "CONFIRMED"

            // Tip: Generate Getters and Setters in IntelliJ by pressing Alt+Insert
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getStatus() { return status; }
            public void setStatus(String status) { this.status = status; }
        }