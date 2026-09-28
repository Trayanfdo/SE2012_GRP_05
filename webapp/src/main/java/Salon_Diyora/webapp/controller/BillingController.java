package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.repository.ServiceItemRepository; // You must create this interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin
public class BillingController {

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @GetMapping
    public List<SalonServiceItem> getAllServices() {
        return serviceItemRepository.findAll();
    }

    @PostMapping
    public SalonServiceItem addService(@RequestBody SalonServiceItem newService) {
        return serviceItemRepository.save(newService);
    }
}