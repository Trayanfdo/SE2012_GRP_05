package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.Appointment;
import Salon_Diyora.webapp.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/stylist/{stylistId}")
    public List<Appointment> getStylistAppointments(@PathVariable Long stylistId) {
        return appointmentRepository.findByStylistIdAndStatusNot(stylistId, "CANCELLED");
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        boolean alreadyBooked = appointmentRepository.isSlotBooked(
                appointment.getStylistId(),
                appointment.getAppointmentTime()
        );

        if (alreadyBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ Selected slot is already booked for this stylist. Please choose another time slot.");
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus("CONFIRMED");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(savedAppointment);
    }
}