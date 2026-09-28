package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/services")
    public List<SalonServiceItem> getCatalog() {
        return billingService.getAllServices();
    }

    @PostMapping("/services")
    public SalonServiceItem createService(@RequestBody SalonServiceItem item) {
        return billingService.addService(item);
    }

    @GetMapping("/bills")
    public List<Bill> getBills() {
        return billingService.getAllBills();
    }

    @PostMapping("/bills")
    public Bill createBill(@RequestBody Bill bill) {
        return billingService.generateBill(bill);
    }
}