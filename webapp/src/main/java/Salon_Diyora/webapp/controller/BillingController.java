package Salon_Diyora.webapp.controller;

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

    @GetMapping
    public List<SalonServiceItem> getAllServices() {
        return billingService.getAllServices();
    }

    @PostMapping
    public SalonServiceItem addService(@RequestBody SalonServiceItem newService) {
        return billingService.addService(newService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalonServiceItem> updateService(@PathVariable Long id, @RequestBody SalonServiceItem updatedService) {
        SalonServiceItem service = billingService.updateService(id, updatedService);
        return service != null ? ResponseEntity.ok(service) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        boolean deleted = billingService.deleteService(id);
        return deleted ? ResponseEntity.ok("Service deleted successfully.") : ResponseEntity.notFound().build();
    }
}