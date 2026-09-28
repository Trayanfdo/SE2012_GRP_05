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

    public SalonServiceItem addService(SalonServiceItem serviceItem) {
        return serviceItemRepository.save(serviceItem);
    }

    public List<SalonServiceItem> getAllServices() {
        return serviceItemRepository.findAll();
    }

    public Bill generateBill(Bill bill) {
        if (bill.getBillingDate() == null) {
            bill.setBillingDate(LocalDateTime.now());
        }
        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}