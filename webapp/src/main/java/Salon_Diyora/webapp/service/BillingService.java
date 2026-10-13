package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Appointment;
import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.repository.AppointmentRepository;
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
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public BillingService(ServiceItemRepository serviceItemRepository,
                          BillRepository billRepository,
                          AppointmentRepository appointmentRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.billRepository = billRepository;
        this.appointmentRepository = appointmentRepository;
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

    // AUTOMATIC BILL GENERATION WITH DYNAMIC TOTAL CALCULATION
    public Bill generateBill(Bill bill) {
        if (bill.getBillingDate() == null) {
            bill.setBillingDate(LocalDateTime.now());
        }

        // If totalAmount is 0 or unassigned, compute the total from selected services
        if (bill.getTotalAmount() <= 0 && bill.getAppointmentId() != null) {
            double computedTotal = 0.0;
            Appointment appointment = appointmentRepository.findById(bill.getAppointmentId()).orElse(null);

            if (appointment != null && appointment.getServiceIds() != null && !appointment.getServiceIds().trim().isEmpty()) {
                String[] ids = appointment.getServiceIds().split(",");
                for (String idStr : ids) {
                    try {
                        Long sId = Long.parseLong(idStr.trim());
                        SalonServiceItem item = serviceItemRepository.findById(sId).orElse(null);
                        if (item != null) {
                            computedTotal += item.getPrice();
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

            bill.setTotalAmount(computedTotal);
        }

        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}