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
    private Long serviceId;
    private LocalDateTime appointmentTime;
    private String status; // "CONFIRMED", "COMPLETED", "CANCELLED"

    public Appointment() {}

    public Appointment(Long customerId, Long stylistId, Long serviceId, LocalDateTime appointmentTime, String status) {
        this.customerId = customerId;
        this.stylistId = stylistId;
        this.serviceId = serviceId;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getStylistId() { return stylistId; }
    public void setStylistId(Long stylistId) { this.stylistId = stylistId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}