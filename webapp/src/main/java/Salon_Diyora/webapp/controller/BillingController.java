package Salon_Diyora.webapp.controller;

import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.service.BillingService;
import Salon_Diyora.webapp.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService billingService;
    private final PdfService pdfService;

    @Autowired
    public BillingController(BillingService billingService, PdfService pdfService) {
        this.billingService = billingService;
        this.pdfService = pdfService;
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
    public ResponseEntity<SalonServiceItem> updateService(@PathVariable Long id, @RequestBody SalonServiceItem service) {
        SalonServiceItem updated = billingService.updateService(id, service);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        boolean deleted = billingService.deleteService(id);
        return deleted ? ResponseEntity.ok("Service deleted successfully.") : ResponseEntity.notFound().build();
    }

    // POST endpoint to generate bill
    @PostMapping("/bills")
    public Bill generateBill(@RequestBody Bill bill) {
        return billingService.generateBill(bill);
    }

    // GET endpoint to fetch all bills
    @GetMapping("/bills")
    public List<Bill> getAllBills() {
        return billingService.getAllBills();
    }

    // GET endpoint for PDF Invoice Download (Fixes 404 Error)
    @GetMapping("/bills/{id}/pdf")
    public ResponseEntity<InputStreamResource> downloadInvoicePdf(@PathVariable Long id) {
        ByteArrayInputStream pdfStream = pdfService.generateInvoicePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=Salon_Diyora_Invoice_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }
}