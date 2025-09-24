package com.mncedicy.stims.api.Services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mncedicy.stims.api.Classes.CourtRollData;
import com.mncedicy.stims.api.Classes.InfringementData;
import com.mncedicy.stims.api.Classes.InvoiceData;
import com.mncedicy.stims.api.Model.Client;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    public PdfService() {
    }

    // Converts HTML string to byte array
    private byte[] generatePdfFromHtml(String html) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, output);
        return output.toByteArray();
    }

    public void generatePdfFromHtmlToFile(String html, String fileName, String outputFolder) {
        try {

            outputFolder = createFolder(outputFolder) + "//" + fileName;
            OutputStream outputStream = new FileOutputStream(outputFolder);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<byte[]> downloadResponseEntity(String fileName, String outputFolder, byte[] pdf) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.add("location", outputFolder + "//" + fileName);
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    public String generateBarcode(String data) throws Exception {
        Barcode barcode = BarcodeFactory.createCode128(data);
        BufferedImage bufferedImage = BarcodeImageHandler.getImage(barcode);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(bufferedImage, "png", outputStream);
        byte[] barcodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(barcodeBytes);
    }

    public String createFolder(String folderPath) {
        Path path = Paths.get(folderPath);
        try {
            Files.createDirectories(path);
            System.out.println("Folder created");
        } catch (IOException e) {
            System.err.println("already exists");
        }
        return folderPath;
    }

    public ResponseEntity<Resource> downloadNoticeExcel(HSSFWorkbook workbook, int client_id) {

        String stamp = LocalDateTime.now().getMinute() + "";
        String outputFolder = createFolder("attachments\\client_" + client_id + "\\report") + "\\notices.xls";

        try {
            OutputStream ops = new FileOutputStream(outputFolder);
            workbook.write(ops);
            workbook.close();
            ops.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "notices.xls");
        headers.add("name", "notices.xls");
        InputStreamResource resource;
        try {

            resource = new InputStreamResource(new FileInputStream(outputFolder));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header("name", "notices.xls")
                .headers(headers)
                .body(resource);

    }

    public ResponseEntity<byte[]> downloadCourtRollResponseEntity(CourtRollData courtRollData, String type) {

        String subscriptionPdfHtml = parseCourtRollTemplate(courtRollData, type);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = "court_roll_" + courtRollData.court_roll.court_roll_id + ".pdf";
        String outputFolder = "attachments\\client_" + courtRollData.court_roll.court_roll_client_id + "\\court_roll";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parseCourtRollTemplate(CourtRollData courtRollData, String type) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");

        Context context = new Context();
        Map<String, Object> templateVariables = Map.of(
                "court_roll_items", courtRollData.court_roll_items,
                "court_roll", courtRollData.court_roll,
                "type", type);

        context.setVariables(templateVariables);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/CourtRollTemplate", context);
    }

    public ResponseEntity<byte[]> downloadInvoiceResponseEntity(InvoiceData invoiceData) {

        String subscriptionPdfHtml = parsePaymentReceiptTemplate(invoiceData);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = "payment_receipt_" + invoiceData.invoice.invoice_number + ".pdf";
        String outputFolder = "attachments\\client_" + invoiceData.invoice.invoice_client_id + "\\payment_receipt";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parsePaymentReceiptTemplate(InvoiceData invoiceData) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // templateResolver.setTemplateMode("XHTML");
        templateResolver.setSuffix(".html");

        Context context = new Context();
        Map<String, Object> templateVariables = Map.of(
                "invoice_items", invoiceData.invoice_items,
                "invoice_name_from", invoiceData.invoice.invoice_name_from,
                "invoice_name_to", invoiceData.invoice.invoice_name_to,
                "invoice_number", invoiceData.invoice.invoice_number,
                "invoice_amount", invoiceData.invoice.invoice_amount,
                "invoice_payment_type", invoiceData.invoice.invoice_payment_type,
                "invoice_payment_date", invoiceData.invoice.invoice_payment_date);

        context.setVariables(templateVariables);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/PaymentReceiptTemplate", context);
    }

    public ResponseEntity<byte[]> downloadNoticeReport(Map<String, Object> templateVariables) {

        String subscriptionPdfHtml = parseNoticeReportTemplate(templateVariables);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = "report.pdf";
        String outputFolder = "attachments\\client_" + templateVariables.get("client_id") + "\\notice_reports";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parseNoticeReportTemplate(Map<String, Object> templateVariables) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        Context context = new Context();
        context.setVariables(templateVariables);
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/NoticeReportTemplate", context);
    }

    public ResponseEntity<byte[]> downloadPaymentReport(Map<String, Object> templateVariables) {

        String subscriptionPdfHtml = parsePaymentReportTemplate(templateVariables);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = "report.pdf";
        String outputFolder = "attachments\\client_" + templateVariables.get("client_id") + "\\payment_reports";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parsePaymentReportTemplate(Map<String, Object> templateVariables) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        Context context = new Context();
        context.setVariables(templateVariables);
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/PaymentReportTemplate", context);
    }

    public ResponseEntity<byte[]> downloadPrintOutstanding(Map<String, Object> templateVariables) {

        String subscriptionPdfHtml = parsePrintOutstandingTemplate(templateVariables);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = templateVariables.get("id_number") + ".pdf";
        String outputFolder = "attachments\\client_" + templateVariables.get("client_id") + "\\outstanding_tickets";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parsePrintOutstandingTemplate(Map<String, Object> templateVariables) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        Context context = new Context();
        context.setVariables(templateVariables);
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/OutstandingTicketsTemplate", context);
    }

    public ResponseEntity<byte[]> downloadLegalLetterResponseEntity(List<InfringementData> infringementDataList,
            String letter_type, Client client) {

        String subscriptionPdfHtml = parseLegalLetterTemplate(infringementDataList, letter_type, client);
        byte[] pdf = generatePdfFromHtml(subscriptionPdfHtml);
        String fileName = letter_type.split(" ")[0] + "_" + infringementDataList.get(0).notice.infringement_notice_id
                + ".pdf";
        String outputFolder = "attachments\\client_" + client.client_id + "\\legal_letter";
        generatePdfFromHtmlToFile(subscriptionPdfHtml, fileName, outputFolder);
        return downloadResponseEntity(fileName, outputFolder, pdf);
    }

    private String parseLegalLetterTemplate(List<InfringementData> infringementDataList, String letter_type,
            Client client) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");
        List<String> barcodes = new ArrayList<>();
        for (InfringementData data : infringementDataList) {
            try {
                barcodes.add(generateBarcode(data.notice.infringement_notice_reference));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Context context = new Context();
        Map<String, Object> templateVariables = Map.of(
                "infringementDataList", infringementDataList,
                "letter_type", letter_type,
                "client", client,
                "barcodes", barcodes);

        context.setVariables(templateVariables);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("templates/LegalLetterTemplate", context);
    }

}