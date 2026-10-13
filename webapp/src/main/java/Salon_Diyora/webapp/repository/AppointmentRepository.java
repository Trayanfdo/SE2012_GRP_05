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

    // 1. Resolves: Cannot resolve method 'findByStylistIdAndStatusNot'
    // Fetches all appointments for a stylist except those with a specific status (e.g. 'CANCELLED')
    List<Appointment> findByStylistIdAndStatusNot(Long stylistId, String status);

    // 2. Fetches appointments assigned to a stylist by exact status (e.g. 'CONFIRMED')
    List<Appointment> findByStylistIdAndStatus(Long stylistId, String status);

    // 3. Fetches appointments for a customer by status
    List<Appointment> findByCustomerIdAndStatus(Long customerId, String status);

    // 4. Fetches all appointments by status (e.g. 'COMPLETED' for Receptionist billing)
    List<Appointment> findByStatus(String status);

    // 5. Fetches all appointments for a specific stylist
    List<Appointment> findByStylistId(Long stylistId);

    // 6. Double-booking check query (only 'CONFIRMED' status locks a slot)
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.stylistId = :stylistId AND a.appointmentTime = :appointmentTime AND a.status = 'CONFIRMED'")
    boolean isSlotBooked(@Param("stylistId") Long stylistId, @Param("appointmentTime") LocalDateTime appointmentTime);
}