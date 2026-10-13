package Salon_Diyora.webapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long stylistId;

    // Store comma-separated service IDs (e.g., "1,2,5") or total calculated service details
    private String serviceIds;

    private LocalDateTime appointmentTime;
    private String status; // "CONFIRMED", "COMPLETED", "CANCELLED"

    public Appointment() {}

    public Appointment(Long customerId, Long stylistId, String serviceIds, LocalDateTime appointmentTime, String status) {
        this.customerId = customerId;
        this.stylistId = stylistId;
        this.serviceIds = serviceIds;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getStylistId() { return stylistId; }
    public void setStylistId(Long stylistId) { this.stylistId = stylistId; }

    public String getServiceIds() { return serviceIds; }
    public void setServiceIds(String serviceIds) { this.serviceIds = serviceIds; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}