package Salon_Diyora.webapp.service;

import Salon_Diyora.webapp.model.Bill;
import Salon_Diyora.webapp.repository.BillRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final BillRepository billRepository;

    @Autowired
    public PdfService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public ByteArrayInputStream generateInvoicePdf(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

        Document document = new Document(PageSize.A4, 36, 36, 36, 36); // Margins: Left, Right, Top, Bottom
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. BRAND COLORS & FONTS
            BaseColor primaryPurple = new BaseColor(89, 54, 162);   // Salon Diyora Theme (#5936a2)
            BaseColor darkGray = new BaseColor(51, 51, 51);
            BaseColor lightGray = new BaseColor(245, 245, 247);

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryPurple);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, darkGray);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, darkGray);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, primaryPurple);

            // 2. SALON HEADER SECTION
            Paragraph headerTitle = new Paragraph("SALON DIYORA", titleFont);
            headerTitle.setAlignment(Element.ALIGN_LEFT);
            document.add(headerTitle);

            Paragraph subTitle = new Paragraph("Premium Hair, Skin, Nail Care & Spa Treatments\nPhone: +94 77 111 2233 | Email: info@salondiyora.lk", bodyFont);
            subTitle.setSpacingAfter(15);
            document.add(subTitle);

            // HORIZONTAL DIVIDER
            Paragraph divider = new Paragraph("_______________________________________________________________________________",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, primaryPurple));
            divider.setSpacingAfter(15);
            document.add(divider);

            // 3. INVOICE META DETAILS TABLE (2 Columns)
            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = bill.getBillingDate() != null ? bill.getBillingDate().format(formatter) : "N/A";

            PdfPCell leftCell = new PdfPCell(new Phrase("INVOICE #: INV-" + bill.getId() + "\nDate: " + formattedDate, bodyFont));
            leftCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell rightCell = new PdfPCell(new Phrase("Customer ID: #" + bill.getCustomerId() + "\nAppointment ID: #" + bill.getAppointmentId(), bodyFont));
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setBorder(Rectangle.NO_BORDER);

            metaTable.addCell(leftCell);
            metaTable.addCell(rightCell);
            metaTable.setSpacingAfter(20);
            document.add(metaTable);

            // 4. ITEM DETAILS TABLE
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2}); // Column ratios

            // Table Headers
            addTableCell(table, "Item #", headerFont, primaryPurple, Element.ALIGN_CENTER);
            addTableCell(table, "Description / Services Rendered", headerFont, primaryPurple, Element.ALIGN_LEFT);
            addTableCell(table, "Amount (LKR)", headerFont, primaryPurple, Element.ALIGN_RIGHT);

            // Table Content Row
            addTableCell(table, "1", bodyFont, lightGray, Element.ALIGN_CENTER);
            addTableCell(table, "Salon Services Package (Completed Appointment #" + bill.getAppointmentId() + ")", bodyFont, lightGray, Element.ALIGN_LEFT);
            addTableCell(table, String.format("Rs. %,.2f", bill.getTotalAmount()), bodyFont, lightGray, Element.ALIGN_RIGHT);

            table.setSpacingAfter(20);
            document.add(table);

            // 5. TOTAL & FOOTER SECTION
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);

            PdfPCell footerNote = new PdfPCell(new Phrase("Thank you for choosing Salon Diyora!\nPlease visit again.", bodyFont));
            footerNote.setBorder(Rectangle.NO_BORDER);

            PdfPCell grandTotalCell = new PdfPCell(new Phrase("Total Billed: Rs. " + String.format("%,.2f", bill.getTotalAmount()), totalFont));
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

    // Helper method for styling table cells
    private void addTableCell(PdfPTable table, String text, Font font, BaseColor bgColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }
}