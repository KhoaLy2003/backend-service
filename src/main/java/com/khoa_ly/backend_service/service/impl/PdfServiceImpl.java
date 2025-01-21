package com.khoa_ly.backend_service.service.impl;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.khoa_ly.backend_service.exception.ServiceException;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Contract;
import com.khoa_ly.backend_service.service.PdfService;
import com.khoa_ly.backend_service.util.TextFooterEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PdfServiceImpl")
public class PdfServiceImpl implements PdfService {

    @Value("${company.name}")
    private String companyName;

    @Value("${company.address}")
    private String companyAddress;

    @Value("${contract.content.file}")
    private String contentFile;

    @Value("${company.logo.file}")
    private String logoFile;

    @Value("${regular.font.path}")
    private String regularFontPath;

    @Value("${bold.font.path}")
    private String boldFontPath;

    private static Cell createCell(int rowspan, int colspan, String title, String content, Style boldStyle, Style regularStyle) {
        return new Cell(rowspan, colspan)
                .add(new Paragraph()
                        .add(new Text(title).addStyle(boldStyle))
                        .add(new Text(content).addStyle(regularStyle))
                ).setBorder(Border.NO_BORDER);
    }

    @Override
    public String generateContractPdf(Account account, Contract contract) {
        StringBuilder builder = new StringBuilder();
        builder.append("./contract/");
        builder.append(account.getFirstName());
        builder.append(account.getLastName());
        builder.append(".pdf");

        String dest = builder.toString().replaceAll("\\s", "");
        builder.setLength(0);

        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdfDoc = new PdfDocument(writer)) {

            String content = Files.readString(Paths.get(contentFile));
            PdfFont regularFont = PdfFontFactory.createFont(regularFontPath, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            PdfFont boldFont = PdfFontFactory.createFont(boldFontPath, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

            Style regularStyle = new Style()
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setFontColor(ColorConstants.BLACK);

            Style boldStyle = new Style()
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setFontColor(ColorConstants.BLACK);

            Text title = new Text("Employee Hiring Contract")
                    .setFont(boldFont)
                    .setFontSize(16);

            Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
            table.setMarginTop(5);
            table.setBorder(Border.NO_BORDER);

//            table.addCell(createCell(1, 1, "Last Name: ", "Nguyễn Văn A", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "First Name: ", "Nguyễn", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "Date of Birth: ", "dd/mm/yyyy", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "ID: ", "01234567890", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "Phone: ", "01234567890", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "Email: ", "abc@gmail.com", boldStyle, regularStyle));
//
//            table.addCell(createCell(1, 2, "Address: ", "Ho Chi Minh, Viet Nam", boldStyle, regularStyle));
//            table.addCell(createCell(1, 1, "Position: ", "Fresher Developer", boldStyle, regularStyle));

            long duration = ChronoUnit.DAYS.between(contract.getStartDate().toInstant(), contract.getEndDate().toInstant());

            String filledContent = content
                    .replace("[CONTRACT_NUMBER]", contract.getContractNumber())
                    .replace("[DATE]", LocalDate.now().toString())
                    .replace("[COMPANY_NAME]", companyName)
                    .replace("[COMPANY_ADDRESS]", companyAddress)
                    .replace("[EMPLOYEE_NAME]", account.getFirstName() + " " + account.getLastName())
                    .replace("[EMPLOYEE_ADDRESS]", account.getAddress())
                    .replace("[EMPLOYEE_POSITION]", account.getRole().toString())
                    .replace("[START_DATE]", contract.getStartDate().toString())
                    .replace("[END_DATE]", contract.getEndDate().toString())
                    .replace("[SALARY]", contract.getSalary().toString())
                    .replace("[DURATION]", duration + " days");

            ImageData data = ImageDataFactory.create(logoFile);
            Image image = new Image(data);
            image.scaleToFit(60, 60).setHorizontalAlignment(HorizontalAlignment.CENTER);

            Document document = new Document(pdfDoc);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TextFooterEventHandler(document, regularFont));
            document.setMargins(36, 80, 36, 80);
            document.setFont(regularFont);
            document.add(image);
            document.add(new Paragraph(title).setTextAlignment(TextAlignment.CENTER));

            String[] paragraphs = filledContent.split("\n");
            boolean isSignaturePart = false;

            for (String paragraph : paragraphs) {
                if (paragraph.contains("[Signatures]")) {
                    isSignaturePart = true;

                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().add(new Paragraph().add(new Text("Signatures").addStyle(boldStyle))).setBorder(Border.NO_BORDER));
                    continue;
                }

                if (isSignaturePart) {
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                    table.addCell(new Cell(1, 2).add(new Paragraph().add(new Text(paragraph).addStyle(regularStyle))).setBorder(Border.NO_BORDER));
                } else {
                    document.add(new Paragraph(paragraph).addStyle(regularStyle));
                }
            }

            document.add(table);

            return dest;
        } catch (IOException e) {
            log.error("Error while generating PDF: {}", e.getMessage());
            throw new ServiceException("Error while generating PDF");
        }
    }
}
