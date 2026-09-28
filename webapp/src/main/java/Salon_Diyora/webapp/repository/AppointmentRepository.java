package Salon_Diyora.webapp.repository;

import Salon_Diyora.webapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Spring Boot automatically writes the basic CRUD SQL queries for you
}