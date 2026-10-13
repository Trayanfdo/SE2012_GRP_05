package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.repository.BillRepository;
import Salon_Diyora.webapp.repository.ServiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    private final ServiceItemRepository serviceItemRepository;
    private final BillRepository billRepository;

    @Autowired
    public BillingService(ServiceItemRepository serviceItemRepository, BillRepository billRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.billRepository = billRepository;
    }

    public List<SalonServiceItem> getAllServices() {
        return serviceItemRepository.findAll();
    }

    public SalonServiceItem addService(SalonServiceItem serviceItem) {
        return serviceItemRepository.save(serviceItem);
    }

    public SalonServiceItem updateService(Long id, SalonServiceItem updatedService) {
        return serviceItemRepository.findById(id).map(existing -> {
            existing.setServiceName(updatedService.getServiceName());
            existing.setPrice(updatedService.getPrice());
            existing.setDurationMinutes(updatedService.getDurationMinutes());
            return serviceItemRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteService(Long id) {
        if (serviceItemRepository.existsById(id)) {
            serviceItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Fixes 'cannot find symbol method generateBill'
    public Bill generateBill(Bill bill) {
        if (bill.getBillingDate() == null) {
            bill.setBillingDate(LocalDateTime.now());
        }
        return billRepository.save(bill);
    }

    // Fixes 'cannot find symbol method getAllBills'
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}