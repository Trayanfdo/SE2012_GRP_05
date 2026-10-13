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

    // Get all appointments assigned to a specific stylist
    @GetMapping("/stylist/{stylistId}")
    public List<Appointment> getStylistAppointments(@PathVariable Long stylistId) {
        return appointmentRepository.findByStylistIdAndStatusNot(stylistId, "CANCELLED");
    }

    // Get upcoming appointments for a specific customer
    @GetMapping("/customer/{customerId}/upcoming")
    public List<Appointment> getUpcomingCustomerAppointments(@PathVariable Long customerId) {
        return appointmentRepository.findByCustomerIdAndStatus(customerId, "CONFIRMED");
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        boolean alreadyBooked = appointmentRepository.isSlotBooked(
                appointment.getStylistId(),
                appointment.getAppointmentTime()
        );

        if (alreadyBooked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Selected slot is already booked for this stylist.");
        }

        if (appointment.getStatus() == null) {
            appointment.setStatus("CONFIRMED");
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    // Endpoint for updating appointment status (COMPLETED / CANCELLED)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(app -> {
            app.setStatus(status.toUpperCase());
            appointmentRepository.save(app);
            return ResponseEntity.ok("Appointment #" + id + " marked as " + status.toUpperCase());
        }).orElse(ResponseEntity.notFound().build());
    }
}