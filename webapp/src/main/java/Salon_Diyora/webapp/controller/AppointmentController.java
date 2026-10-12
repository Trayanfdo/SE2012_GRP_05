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

    // Fetch all appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Fetch active/upcoming appointments for a specific stylist
    @GetMapping("/stylist/{stylistId}")
    public List<Appointment> getStylistAppointments(@PathVariable Long stylistId) {
        return appointmentRepository.findByStylistIdAndStatusNot(stylistId, "CANCELLED");
    }

    // Book a new appointment with double-booking prevention
    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        boolean alreadyBooked = appointmentRepository.isSlotBooked(
                appointment.getStylistId(),
                appointment.getAppointmentTime()
        );

        if (alreadyBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ Selected slot is already booked for this stylist.");
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus("CONFIRMED");
        }

        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.ok(saved);
    }

    // Stylist Status Update Endpoint (e.g., COMPLETED or CANCELLED)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(status.toUpperCase());
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment status updated to " + status.toUpperCase());
        }).orElse(ResponseEntity.notFound().build());
    }
}