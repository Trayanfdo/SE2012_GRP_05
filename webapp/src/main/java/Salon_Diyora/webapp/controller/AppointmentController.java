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
        return appointmentRepository.findByStylistIdAndStatus(stylistId, "CONFIRMED");
    }

    @GetMapping("/customer/{customerId}/completed")
    public List<Appointment> getCompletedCustomerAppointments(@PathVariable Long customerId) {
        return appointmentRepository.findByCustomerIdAndStatus(customerId, "COMPLETED");
    }

    @GetMapping("/completed")
    public List<Appointment> getAllCompletedAppointments() {
        return appointmentRepository.findByStatus("COMPLETED");
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        boolean alreadyBooked = appointmentRepository.isSlotBooked(
                appointment.getStylistId(),
                appointment.getAppointmentTime()
        );

        if (alreadyBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ Selected time slot is already booked for this stylist.");
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus("CONFIRMED");
        }

        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(app -> {
            app.setStatus(status.toUpperCase());
            appointmentRepository.save(app);
            return ResponseEntity.ok("Appointment #" + id + " marked as " + status.toUpperCase());
        }).orElse(ResponseEntity.notFound().build());
    }
}