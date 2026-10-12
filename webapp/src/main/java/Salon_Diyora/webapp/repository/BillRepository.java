package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAppointmentId(Long appointmentId);
}