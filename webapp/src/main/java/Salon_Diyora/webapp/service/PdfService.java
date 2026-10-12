package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Appointment;
import Salon_Diyora.webapp.model.AppUser;
import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.model.SalonServiceItem;
import Salon_Diyora.webapp.repository.AppointmentRepository;
import Salon_Diyora.webapp.repository.BillRepository;
import Salon_Diyora.webapp.repository.ServiceItemRepository;
import Salon_Diyora.webapp.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    @Autowired private BillRepository billRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ServiceItemRepository serviceItemRepository;

    public ByteArrayInputStream generateInvoicePdf(Long billId) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        Appointment appointment = appointmentRepository.findById(bill.getAppointmentId()).orElse(new Appointment());
        AppUser customer = userRepository.findById(bill.getCustomerId()).orElse(new AppUser("Walk-in Customer", "-", "-", "CUSTOMER"));
        SalonServiceItem service = serviceItemRepository.findById(appointment.getServiceId()).orElse(new SalonServiceItem("Salon Treatment", bill.getTotalAmount(), 30));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.MAGENTA);
            Paragraph header = new Paragraph("Salon Diyora - Payment Invoice", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Invoice ID: #INV-" + bill.getId()));
            document.add(new Paragraph("Date: " + bill.getBillingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            document.add(new Paragraph("Customer Name: " + customer.getName()));
            document.add(new Paragraph("Phone: " + customer.getPhone()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            PdfPCell c1 = new PdfPCell(new Phrase("Service Name"));
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Duration"));
            table.addCell(c1);
            c1 = new PdfPCell(new Phrase("Price (LKR)"));
            table.addCell(c1);

            table.addCell(service.getServiceName());
            table.addCell(service.getDurationMinutes() + " mins");
            table.addCell("Rs. " + service.getPrice());

            document.add(table);
            document.add(new Paragraph(" "));

            Paragraph total = new Paragraph("Total Paid: Rs. " + bill.getTotalAmount(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.GREEN));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}