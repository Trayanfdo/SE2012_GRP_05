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
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ServiceItemRepository serviceItemRepository;

    @Autowired
    public PdfService(BillRepository billRepository,
                      AppointmentRepository appointmentRepository,
                      UserRepository userRepository,
                      ServiceItemRepository serviceItemRepository) {
        this.billRepository = billRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    public ByteArrayInputStream generateInvoicePdf(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

        // Fetch associated appointment details
        Appointment appointment = appointmentRepository.findById(bill.getAppointmentId())
                .orElse(new Appointment());

        // Fetch Customer details
        AppUser customer = userRepository.findById(bill.getCustomerId())
                .orElse(new AppUser("Walk-in Customer", "-", "-", "CUSTOMER"));

        // Fetch Stylist details using stylistId from Appointment
        String stylistName = "Unassigned";
        if (appointment.getStylistId() != null) {
            AppUser stylist = userRepository.findById(appointment.getStylistId()).orElse(null);
            if (stylist != null) {
                stylistName = stylist.getName();
            }
        }

        // Fetch all selected services by parsing comma-separated serviceIds
        List<SalonServiceItem> selectedServices = new ArrayList<>();
        if (appointment.getServiceIds() != null && !appointment.getServiceIds().trim().isEmpty()) {
            String[] ids = appointment.getServiceIds().split(",");
            for (String idStr : ids) {
                try {
                    Long sId = Long.parseLong(idStr.trim());
                    serviceItemRepository.findById(sId).ifPresent(selectedServices::add);
                } catch (NumberFormatException ignored) {}
            }
        }

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. BRAND COLORS & FONTS
            BaseColor primaryPurple = new BaseColor(89, 54, 162);   // Salon Diyora Theme (#5936a2)
            BaseColor darkGray = new BaseColor(51, 51, 51);
            BaseColor lightGray = new BaseColor(245, 245, 247);
            BaseColor white = BaseColor.WHITE;

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryPurple);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, white);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, darkGray);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, primaryPurple);

            // 2. SALON HEADER
            Paragraph headerTitle = new Paragraph("SALON DIYORA", titleFont);
            headerTitle.setAlignment(Element.ALIGN_LEFT);
            document.add(headerTitle);

            Paragraph subTitle = new Paragraph("Premium Hair, Skin, Nail Care & Spa Treatments\nPhone: +94 77 159 7530 | Email: info@salondiyora.lk", bodyFont);
            subTitle.setSpacingAfter(15);
            document.add(subTitle);

            // DIVIDER LINE
            Paragraph divider = new Paragraph("_______________________________________________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, primaryPurple));
            divider.setSpacingAfter(15);
            document.add(divider);

            // 3. INVOICE META DETAILS (Includes Stylist Name)
            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = bill.getBillingDate() != null ? bill.getBillingDate().format(formatter) : "N/A";

            String leftText = "INVOICE #: INV-" + bill.getId() + "\n" +
                    "Date: " + formattedDate + "\n" +
                    "Customer Name: " + customer.getName() + "\n" +
                    "Phone: " + customer.getPhone();
            PdfPCell leftCell = new PdfPCell(new Phrase(leftText, bodyFont));
            leftCell.setBorder(Rectangle.NO_BORDER);

            String rightText = "Appointment ID: #" + bill.getAppointmentId() + "\n" +
                    "Assigned Stylist: " + stylistName;

            PdfPCell rightCell = new PdfPCell(new Phrase(rightText, bodyFont));
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setBorder(Rectangle.NO_BORDER);

            metaTable.addCell(leftCell);
            metaTable.addCell(rightCell);
            metaTable.setSpacingAfter(20);
            document.add(metaTable);

            // 4. ITEM DETAILS TABLE
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2});

            // Table Headers
            addTableCell(table, "Item #", headerFont, primaryPurple, Element.ALIGN_CENTER);
            addTableCell(table, "Service Name", headerFont, primaryPurple, Element.ALIGN_LEFT);
            addTableCell(table, "Amount (LKR)", headerFont, primaryPurple, Element.ALIGN_RIGHT);

            double calculatedTotal = 0.0;

            // Render each selected service line-by-line
            if (!selectedServices.isEmpty()) {
                int itemIndex = 1;
                for (SalonServiceItem service : selectedServices) {
                    addTableCell(table, String.valueOf(itemIndex++), bodyFont, lightGray, Element.ALIGN_CENTER);
                    addTableCell(table, service.getServiceName(), bodyFont, lightGray, Element.ALIGN_LEFT);
                    addTableCell(table, String.format("Rs. %,.2f", service.getPrice()), bodyFont, lightGray, Element.ALIGN_RIGHT);
                    calculatedTotal += service.getPrice();
                }
            } else {
                // Fallback if no specific services found in DB
                addTableCell(table, "1", bodyFont, lightGray, Element.ALIGN_CENTER);
                addTableCell(table, "Salon Services Package (Appointment #" + bill.getAppointmentId() + " - Stylist: " + stylistName + ")", bodyFont, lightGray, Element.ALIGN_LEFT);
                addTableCell(table, String.format("Rs. %,.2f", bill.getTotalAmount()), bodyFont, lightGray, Element.ALIGN_RIGHT);
                calculatedTotal = bill.getTotalAmount();
            }

            table.setSpacingAfter(20);
            document.add(table);

            // 5. TOTAL & FOOTER SECTION
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);

            PdfPCell footerNote = new PdfPCell(new Phrase("Thank you for choosing Salon Diyora!\nPlease visit again.", bodyFont));
            footerNote.setBorder(Rectangle.NO_BORDER);

            PdfPCell grandTotalCell = new PdfPCell(new Phrase("Total Billed: Rs. " + String.format("%,.2f", calculatedTotal), totalFont));
            grandTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            grandTotalCell.setBorder(Rectangle.NO_BORDER);

            totalTable.addCell(footerNote);
            totalTable.addCell(grandTotalCell);
            document.add(totalTable);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableCell(PdfPTable table, String text, Font font, BaseColor bgColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }
}