package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.Appointment;
import Salon_Diyora.webapp.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Explicitly mapping GET /api/appointments to return ALL appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> list = appointmentRepository.findAll();
        return ResponseEntity.ok(list);
    }

    // GET upcoming appointments for a specific stylist
    @GetMapping("/stylist/{stylistId}")
    public List<Appointment> getStylistAppointments(@PathVariable Long stylistId) {
        return appointmentRepository.findByStylistIdAndStatusNot(stylistId, "CANCELLED");
    }

    // GET all appointments for a specific customer
    @GetMapping("/customer/{customerId}")
    public List<Appointment> getCustomerAppointments(@PathVariable Long customerId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getCustomerId() != null && a.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    // GET only CONFIRMED appointments for slot availability checks on customer portal
    @GetMapping("/stylist/{stylistId}/booked")
    public List<Appointment> getBookedStylistAppointments(@PathVariable Long stylistId) {
        return appointmentRepository.findByStylistIdAndStatus(stylistId, "CONFIRMED");
    }

    // GET active/upcoming (CONFIRMED) appointments for a specific customer
    @GetMapping("/customer/{customerId}/upcoming")
    public List<Appointment> getUpcomingCustomerAppointments(@PathVariable Long customerId) {
        return appointmentRepository.findByCustomerIdAndStatus(customerId, "CONFIRMED");
    }

    // GET past/old appointments (COMPLETED or CANCELLED) for a specific customer
    @GetMapping("/customer/{customerId}/history")
    public List<Appointment> getCustomerAppointmentHistory(@PathVariable Long customerId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getCustomerId() != null && a.getCustomerId().equals(customerId))
                .filter(a -> a.getStatus() != null &&
                        (a.getStatus().equalsIgnoreCase("COMPLETED") || a.getStatus().equalsIgnoreCase("CANCELLED")))
                .collect(Collectors.toList());
    }

    // POST: Book a new appointment
    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        if (appointment.getStatus() == null) {
            appointment.setStatus("CONFIRMED");
        }
        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.ok(saved);
    }

    // PUT: Update appointment status (COMPLETED / CANCELLED)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(status.toUpperCase());
            appointmentRepository.save(appointment);
            return ResponseEntity.ok("Appointment status updated to " + status.toUpperCase());
        }).orElse(ResponseEntity.notFound().build());
    }
}