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

    List<Appointment> findByStylistIdAndStatusNot(Long stylistId, String status);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.stylistId = :stylistId AND a.appointmentTime = :appointmentTime AND a.status != 'CANCELLED'")
    boolean isSlotBooked(@Param("stylistId") Long stylistId, @Param("appointmentTime") LocalDateTime appointmentTime);
}