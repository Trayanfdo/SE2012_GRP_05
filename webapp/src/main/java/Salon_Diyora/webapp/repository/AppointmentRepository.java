package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Fetch all non-cancelled appointments for a specific stylist
    List<Appointment> findByStylistIdAndStatusNot(Long stylistId, String status);

    // Fetch appointments for a specific stylist by exact status
    List<Appointment> findByStylistIdAndStatus(Long stylistId, String status);

    // Fetch upcoming/confirmed appointments for a specific customer
    List<Appointment> findByCustomerIdAndStatus(Long customerId, String status);

    // Fetch appointments by status (e.g., "COMPLETED" for Receptionist billing)
    List<Appointment> findByStatus(String status);

    // Check double booking
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.stylistId = :stylistId AND a.appointmentTime = :appointmentTime AND a.status = 'CONFIRMED'")
    boolean isSlotBooked(@Param("stylistId") Long stylistId, @Param("appointmentTime") LocalDateTime appointmentTime);
}