package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    // Accessible by all users (Customers, Staff, Owner)
    @GetMapping
    public List<SalonServiceItem> getAllServices() {
        return billingService.getAllServices();
    }

    // Create new service
    @PostMapping
    public SalonServiceItem addService(@RequestBody SalonServiceItem newService) {
        return billingService.addService(newService);
    }

    // Update existing service by ID
    @PutMapping("/{id}")
    public ResponseEntity<SalonServiceItem> updateService(@PathVariable Long id, @RequestBody SalonServiceItem service) {
        SalonServiceItem updated = billingService.updateService(id, service);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Delete service by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        boolean deleted = billingService.deleteService(id);
        return deleted ? ResponseEntity.ok("Service deleted successfully.") : ResponseEntity.notFound().build();
    }
}