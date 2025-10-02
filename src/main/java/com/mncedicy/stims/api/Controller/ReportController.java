package com.mncedicy.stims.api.Controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mncedicy.stims.api.Classes.*;
import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import com.mncedicy.stims.api.Services.EmailServiceImpl;
import com.mncedicy.stims.api.Services.PdfService;
import com.mncedicy.stims.api.Services.PushNotificationService;
import com.mncedicy.stims.api.Services.TwilioService;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;
import org.json.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("ALL")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    private BookBatchRepo bookBatchRepo;
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookItemRepo bookItemRepo;
    @Autowired
    private InfringerRepo infringerRepo;
    @Autowired
    private NoticeRepo noticeRepo;
    @Autowired
    private ChargeCodeRepo chargeCodeRepo;
    @Autowired
    private CourtClientRepo courtClientRepo;
    @Autowired
    private CourtRepo courtRepo;
    @Autowired
    private VehicleColourRepo vehicleColourRepo;
    @Autowired
    private VehicleMakeRepo vehicleMakeRepo;
    @Autowired
    private VehicleModelRepo vehicleModelRepo;
    @Autowired
    private VehicleTypeRepo vehicleTypeRepo;
    @Autowired
    private VehicleUsageRepo vehicleUsageRepo;
    @Autowired
    private HistoryRepo historyRepo;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private NotificationItemRepo notificationItemRepo;
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TwilioService twilioService;
    private final PushNotificationService pushNotificationService;
    private final PdfService pdfService;

    public ReportController(PushNotificationService pushNotificationService, PdfService pdfService) {
        this.pushNotificationService = pushNotificationService;
        this.pdfService = pdfService;
    }

    @GetMapping(value = "/welcome")
    public String getPage() {
        return "Welcome";
    }

    @GetMapping(value = "/getBookCountByOfficerId")
    @ResponseBody
    public String getBookCountByOfficerId(@RequestParam long book_issued_to) {
        JSONObject json = new JSONObject();
        JSONObject jBooks = new JSONObject();
        jBooks.put("all", bookRepo.CountAllByfficerId(book_issued_to));
        jBooks.put("handed_in", bookRepo.CountHandedinByfficerId(book_issued_to));
        json.put("Books", jBooks);
        JSONObject jItem = new JSONObject();
        jItem.put("all", bookItemRepo.CountAllByfficerId(book_issued_to));
        jItem.put("captured", bookItemRepo.CountCapturedByfficerId(book_issued_to));
        json.put("Items", jItem);
        JSONObject jCaptured = new JSONObject();
        jCaptured.put("all", noticeRepo.CountAllByfficerId(book_issued_to));
        jCaptured.put("closed", noticeRepo.CountClosedByfficerId(book_issued_to));
        json.put("Captured", jCaptured);
        return json.toString();
    }

    @GetMapping(value = "/getBookCountAll")
    @ResponseBody
    public String getBookCountAll() {
        JSONObject json = new JSONObject();
        JSONObject jBooks = new JSONObject();
        jBooks.put("all", bookRepo.CountAllIssued());
        jBooks.put("handed_in", bookRepo.CountAllHandedin());
        json.put("Books", jBooks);
        JSONObject jItem = new JSONObject();
        jItem.put("all", bookItemRepo.CountAllIssued());
        jItem.put("captured", bookItemRepo.CountAllCaptured());
        json.put("Items", jItem);
        JSONObject jCaptured = new JSONObject();
        jCaptured.put("all", noticeRepo.CountAllNotice());
        jCaptured.put("closed", noticeRepo.CountAllNoticeClosed());
        json.put("Captured", jCaptured);
        return json.toString();
    }

    @GetMapping(value = "/getNoticeAll")
    @ResponseBody
    public DashboardData getNoticeAll(@RequestParam int client_id) {
        DashboardData data = new DashboardData();
        data.notices = noticeRepo.findByClientId(client_id);
        data.books = bookRepo.findByClientId(client_id);
        data.charge_codes = chargeCodeRepo.findByClientId(client_id);
        data.histories = historyRepo.findByClientId(client_id);
        data.users = userRepo.findByClientId(client_id);
        for (User user : data.users) {
            user.user_password = "";
        }
        return data;
    }

    @GetMapping(value = "/getNotificationCount")
    @ResponseBody
    public String getNotificationCount(@RequestParam long person_id) {
        JSONObject json = new JSONObject();
        JSONObject jNotifications = new JSONObject();
        jNotifications.put("all", notificationRepo.CountAllByReceiver(person_id));
        jNotifications.put("unread", notificationItemRepo.CountReceiverUnread(person_id));
        json.put("Notifications", jNotifications);
        return json.toString();
    }

    @GetMapping(value = "/getNotificationItemReceiverUnread")
    @ResponseBody
    public List<notification_item>  getNotificationItemReceiverUnread(@RequestParam long person_id) {
        return notificationItemRepo.findReceiverUnread(person_id);
    }

    @GetMapping(value = "/getNotifications")
    @ResponseBody
    public List<notification> getNotifications(@RequestParam long person_id) {
        return notificationRepo.findBySenderOrReceiverId(person_id);
    }

    @GetMapping(value = "/getNotificationItems")
    @ResponseBody
    public List<notification_item> getNotificationItems(@RequestParam long notification_id, long receiver_id) {
        List<notification_item> itemss = notificationItemRepo.findByNotificationId(notification_id);
        List<notification_item> items = notificationItemRepo.findByNotificationId(notification_id);

        for (notification_item item : items) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            //notification_item itemCloned = gson.fromJson(gson.toJson(item), notification_item.class);
            notification_item itemCloned = item.clonee();
            if (itemCloned.notification_item_receiver_id == receiver_id) {
                    itemCloned.notification_item_status = "Read";
                    itemCloned.notification_item_read_date = LocalDateTime.now();
                    notificationItemRepo.save(itemCloned);


                notification notification = notificationRepo.findById(notification_id).get();
                if (notification.notification_updated_by != receiver_id) {
                    notification.notification_status = "Read";
                    notificationRepo.save(notification);
                }
            }
        }
        return itemss;
    }

    @GetMapping(value = "/getNotificationItemsByNoticeId")
    @ResponseBody
    public List<notification_item> getNotificationItemsByNoticeId(@RequestParam long notice_id, long receiver_id) {
        List<notification_item> itemss = notificationItemRepo.findByNoticeId(notice_id);
        return itemss;
    }


    @GetMapping(value = "/getNotificationItemsByNoticeIdRead")
    @ResponseBody
    public List<notification_item> getNotificationItemsByNoticeIdRead(@RequestParam long notice_id, long receiver_id) {
        List<notification_item> items = notificationItemRepo.findByNoticeId(notice_id);
        for (notification_item item : items) {
            if (item.notification_item_receiver_id == receiver_id) {
                item.notification_item_status = "Read";
                item.notification_item_read_date = LocalDateTime.now();
                notificationItemRepo.save(item);


                notification notification = notificationRepo.findById(item.notification_item_notification_id).get();
                if (notification.notification_updated_by != receiver_id) {
                    notification.notification_status = "Read";
                    notificationRepo.save(notification);
                }
            }
        }
        return items;
    }





    @PostMapping(value = "/sendMessage")
    public Response sendMessage(@RequestBody notification_item item) {

        Response response = new Response();
        response.setStatus("Error");
        try {

            notification notification = notificationRepo.findById(item.notification_item_notification_id).get();
            notification.notification_last_update = LocalDateTime.now();
            notification.notification_message = item.notification_item_message;
            notification.notification_updated_by = item.notification_item_sender_id;
            notification.notification_status = "Unread";
            notificationRepo.save(notification);
            item.notification_item_sent_date = notification.notification_last_update;
            item = notificationItemRepo.save(item);
            response.setData(item);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping(value = "/sendMessageWeb")
    public Response sendMessageWeb(@RequestBody Long id, Long sender, String sender_name, String message) {
        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice notice = noticeRepo.findById(id).get();
            List<notification> notifications = notificationRepo.findByNoticeId(notice.infringement_notice_id);
            notification notification = new notification();
            if (notifications.isEmpty()) {
                notification.notification_notice_id = notice.infringement_notice_id;
                notification.notification_client_id = notice.infringement_notice_client_id;
                notification.notification_reference = notice.infringement_notice_reference;
                notification.notification_receiver_name = notice.infringement_notice_officer_name;
                notification.notification_receiver_id = notice.infringement_notice_officer_id;
                notification.notification_sender_id = sender;
                notification.notification_sender_name = sender_name;
                notification.notification_message = message;
                notification.notification_subject = notice.infringement_notice_reference;
                notification.notification_type = "infringement";
            } else {
                notification = notifications.get(0);
            }
            notification.notification_status = "Unread";
            notification.notification_last_update = LocalDateTime.now();
            notification.notification_updated_by = sender;
            notification.notification_message = message;
            notification = notificationRepo.save(notification);

            notification_item item = new notification_item();
            item.notification_item_sent_date = notification.notification_last_update;
            item.notification_item_message = message;
            item.notification_item_notification_id = notification.notification_id;
            item.notification_item_notice_id = notice.infringement_notice_id;
            item.notification_item_client_id = notice.infringement_notice_client_id;
            item.notification_item_reference = notice.infringement_notice_reference;
            item.notification_item_receiver_name = notice.infringement_notice_officer_name;
            item.notification_item_receiver_id = notice.infringement_notice_officer_id;
            item.notification_item_sender_id = sender;
            item.notification_item_sender_name = sender_name;
            item.notification_item_subject = notice.infringement_notice_reference;
            item.notification_item_type = "infringement";
            item.notification_item_status = "Unread";
            notificationItemRepo.save(item);

            response.setData(item);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping(value = "/getNoticesLight")
    @ResponseBody
    public List<infringement_notice> getNoticesLight(@RequestParam int client_id, @RequestParam String type_name,
            @RequestParam String date_from, @RequestParam String date_to,
            @RequestParam String access_status) {
        return noticeRepo.findNoticeTypeByDateRange(client_id, type_name, LocalDate.parse(date_from),
                LocalDate.parse(date_to), access_status);
    }

    @GetMapping(value = "/getInvoicesLight")
    @ResponseBody
    public List<invoice> getInvoicesLight(@RequestParam int client_id,
            @RequestParam String date_from, @RequestParam String date_to) {
        return invoiceRepo.findInvoiceDateRange(client_id, LocalDate.parse(date_from), LocalDate.parse(date_to));
    }

    @PostMapping(value = "/printNoticeReport")
    @ResponseBody
    public ResponseEntity<byte[]> printNoticeReport(@RequestBody List<infringement_notice> notices,
            @RequestParam String grouping) {
        List<String> groupings = new ArrayList<>();
        List<Double> group_total = new ArrayList<>();
        List<List<infringement_notice>> noticeList = new ArrayList<>();
        double amountAll = 0, amountPaid = 0;
        int countAll = notices.size(), countPaid = 0, countNAG = 0;
        for (infringement_notice notice : notices) {
            DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMM, yyyy");

            String column = grouping.equals("Traffic Officer") ? notice.infringement_notice_officer_name
                    : grouping.equals("Charge Code") ? notice.infringement_notice_charge_code + ""
                            : grouping.equals("Captured By") ? notice.infringement_notice_captured_by_name
                                    : grouping.equals("Offence Date") ? notice.infringement_notice_offence_date + ""
                                            : grouping.equals("Offence Month")
                                                    ? notice.infringement_notice_offence_date.format(formatterMonth)
                                                    : grouping.equals("Notice Status")
                                                            ? notice.infringement_notice_status
                                                            : grouping.equals("Notice Type")
                                                                    ? notice.infringement_notice_type_name
                                                                    : "Infringements";

            int index = groupings.indexOf(column);
            if (index >= 0) {
                noticeList.get(index).add(notice);
                group_total.set(index, group_total.get(index) + notice.infringement_notice_final_amount);
            } else {
                group_total.add(notice.infringement_notice_final_amount);
                groupings.add(column);
                noticeList.add(new ArrayList<>(List.of(notice)));
            }

            amountAll += notice.infringement_notice_final_amount;
            countNAG += Objects.equals(notice.infringement_notice_fine_amount, "NAG") ? 1 : 0;
            amountPaid += Objects.equals(notice.infringement_notice_status, "Paid")
                    ? notice.infringement_notice_final_amount
                    : 0;
            countPaid += Objects.equals(notice.infringement_notice_status, "Paid") ? 1 : 0;
        }

        Map<String, Object> templateVariables = Map.ofEntries(
                Map.entry("noticeList", noticeList),
                Map.entry("groupings", groupings),
                Map.entry("startDate", notices.get(0).infringement_notice_offence_date),
                Map.entry("endDate", notices.get(notices.size() - 1).infringement_notice_offence_date),
                Map.entry("grouping", grouping),
                Map.entry("amountAll", amountAll),
                Map.entry("countPaid", countPaid),
                Map.entry("amountPaid", amountPaid),
                Map.entry("countAll", countAll),
                Map.entry("group_total", group_total),
                Map.entry("chart", generateChart(groupings, noticeList, grouping)),
                Map.entry("client_id", notices.get(0).infringement_notice_client_id));

        return pdfService.downloadNoticeReport(templateVariables);

    }



    @PostMapping(value = "/printPaymentReport")
    @ResponseBody
    public ResponseEntity<byte[]> printPaymentReport(@RequestBody List<invoice> invoices,
            @RequestParam String grouping) {
        List<String> groupings = new ArrayList<>();
        List<Double> group_total = new ArrayList<>();
        List<List<invoice>> noticeList = new ArrayList<>();
        double amountAll = 0, amountPaid = 0;
        int countAll = invoices.size(), countPaid = 0, countNAG = 0;
        for (invoice invoice : invoices) {
            DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMM, yyyy");

            String column = grouping.equals("Payment Type") ? invoice.invoice_payment_type
                    : grouping.equals("Collected By") ? invoice.invoice_created_by_name
                            : grouping.equals("Payment Date") ? invoice.invoice_payment_date + ""
                                    : grouping.equals("Payment Month")
                                            ? invoice.invoice_payment_date.format(formatterMonth)
                                            : "Invoices";

            int index = groupings.indexOf(column);
            if (index >= 0) {
                noticeList.get(index).add(invoice);
                group_total.set(index, group_total.get(index) + invoice.invoice_amount);
            } else {
                group_total.add(invoice.invoice_amount);
                groupings.add(column);
                noticeList.add(new ArrayList<>(List.of(invoice)));
            }

            amountAll += invoice.invoice_amount;

        }

        Map<String, Object> templateVariables = Map.ofEntries(
                Map.entry("noticeList", noticeList),
                Map.entry("groupings", groupings),
                Map.entry("startDate",
                        invoices.get(0).invoice_payment_date),
                Map.entry("endDate", invoices.get(invoices.size() - 1).invoice_payment_date),
                Map.entry("grouping", grouping),
                Map.entry("amountAll", amountAll),
                Map.entry("countPaid", countPaid),
                Map.entry("amountPaid", amountPaid),
                Map.entry("countAll", countAll),
                Map.entry("group_total", group_total),
                Map.entry("chart",
                        generateChartPayment(groupings, noticeList, grouping)),
                Map.entry("client_id", invoices.get(0).invoice_client_id));

        return pdfService.downloadPaymentReport(templateVariables);

    }

    @PostMapping(value = "/printPaymentExcel")
    @ResponseBody
    public ResponseEntity<Resource> printPaymentExcel(@RequestBody List<invoice> invoices,
            @RequestParam String author) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();

        workbook.createInformationProperties();
        SummaryInformation summaryInfo = workbook.getSummaryInformation();
        summaryInfo.setAuthor(author);
        summaryInfo.setSubject("Notices");
        summaryInfo.setTitle("Notice Report");
        summaryInfo.setCreateDateTime(new java.util.Date());

        HSSFSheet sheet = workbook.createSheet("Notice Report");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.DASHED); // double lines border
        style.setBorderBottom(BorderStyle.DASHED); // single line border
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(Font.COLOR_RED);
        font.setBold(true);
        style.setFont(font);

        HSSFRow row = sheet.createRow(0);
        row.setRowStyle(style);
        row.createCell(0).setCellValue("Invoice Number");
        row.createCell(1).setCellValue("Collected By");
        row.createCell(2).setCellValue("Paid By");
        row.createCell(3).setCellValue("Payment Type");
        row.createCell(4).setCellValue("Tickets");
        row.createCell(5).setCellValue("Payment date");
        row.createCell(6).setCellValue("Amount");

        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        HSSFDataFormat dateFormat = workbook.createDataFormat();
        dateCellStyle.setDataFormat(dateFormat.getFormat("yyyy-mm-dd"));

        int dataRowIndex = 1;
        double total = 0;
        for (invoice invoice : invoices) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(invoice.invoice_number + "");
            dataRow.createCell(1).setCellValue(invoice.invoice_created_by_name);
            dataRow.createCell(2).setCellValue(invoice.invoice_name_to);
            dataRow.createCell(3).setCellValue(invoice.invoice_payment_type);
            dataRow.createCell(4).setCellValue(invoice.invoice_records + "");
            dataRow.createCell(5).setCellValue(invoice.invoice_payment_date);
            dataRow.createCell(6).setCellValue(invoice.invoice_amount);

            if (invoice.invoice_payment_date != null) {
                HSSFCell dateCell = dataRow.createCell(5);
                dateCell.setCellValue(Date.valueOf(invoice.invoice_payment_date));
                dateCell.setCellStyle(dateCellStyle);
            }
            dataRowIndex++;
            total += invoice.invoice_amount;
        }
        HSSFRow dataRow = sheet.createRow(dataRowIndex);
        dataRow.createCell(6).setCellValue(total);

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        return pdfService.downloadNoticeExcel(workbook, invoices.get(0).invoice_client_id,"payment");

    }

    public String generateChartPayment(List<String> groupings, List<List<invoice>> noticeList, String grouping) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < groupings.size(); i++) {
            dataset.addValue(noticeList.get(i).size(), grouping, groupings.get(i));
        }
        // 2. Create a chart

        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset);
        plot.setRenderer(new BarRenderer());
        plot.setDomainAxis(new CategoryAxis(grouping));
        plot.setRangeAxis(new NumberAxis("Invoices"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(true);
        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setSeriesPaint(0, Color.GRAY);
        r.setDrawBarOutline(false);
        r.setShadowVisible(false);
        r.setBarPainter(new StandardBarPainter());
        r.setSeriesVisible(0, true);
        JFreeChart chart = new JFreeChart(null, null, plot, false);

        BufferedImage bufferedImage = chart.createBufferedImage(660, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] barcodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(barcodeBytes);
    }

    public String generateChart(List<String> groupings, List<List<infringement_notice>> noticeList, String grouping) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < groupings.size(); i++) {
            dataset.addValue(noticeList.get(i).size(), grouping, groupings.get(i));
        }
        // 2. Create a chart

        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset);
        plot.setRenderer(new BarRenderer());
        plot.setDomainAxis(new CategoryAxis(grouping));
        plot.setRangeAxis(new NumberAxis("Notices"));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(true);
        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setSeriesPaint(0, Color.GRAY);
        r.setDrawBarOutline(false);
        r.setShadowVisible(false);
        r.setBarPainter(new StandardBarPainter());
        r.setSeriesVisible(0, true);
        JFreeChart chart = new JFreeChart(null, null, plot, false);

        BufferedImage bufferedImage = chart.createBufferedImage(660, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] barcodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(barcodeBytes);
    }

    @PostMapping(value = "/printNoticeExcel")
    @ResponseBody
    public ResponseEntity<Resource> printNoticeExcel(@RequestBody List<infringement_notice> notices,
            @RequestParam String author) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();

        workbook.createInformationProperties();
        SummaryInformation summaryInfo = workbook.getSummaryInformation();
        summaryInfo.setAuthor(author);
        summaryInfo.setSubject("Notices");
        summaryInfo.setTitle("Notice Report");
        summaryInfo.setCreateDateTime(new java.util.Date());

        HSSFSheet sheet = workbook.createSheet("Notice Report");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.DASHED); // double lines border
        style.setBorderBottom(BorderStyle.DASHED); // single line border
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(Font.COLOR_RED);
        font.setBold(true);
        style.setFont(font);

        HSSFRow row = sheet.createRow(0);
        row.setRowStyle(style);
        row.createCell(0).setCellValue("References");
        row.createCell(1).setCellValue("Registration");
        row.createCell(2).setCellValue("Driver");
        row.createCell(3).setCellValue("Officer");
        row.createCell(4).setCellValue("Charge");
        row.createCell(5).setCellValue("Status");
        row.createCell(6).setCellValue("Offence date");
        row.createCell(7).setCellValue("Captured by");
        row.createCell(8).setCellValue("ID number");
        row.createCell(9).setCellValue("Fine");

        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        HSSFDataFormat dateFormat = workbook.createDataFormat();
        dateCellStyle.setDataFormat(dateFormat.getFormat("yyyy-mm-dd"));

        int dataRowIndex = 1;
        double total = 0;
        for (infringement_notice notice : notices) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(notice.infringement_notice_reference);
            dataRow.createCell(1).setCellValue(notice.infringement_notice_registration);
            dataRow.createCell(2).setCellValue(notice.infringement_notice_name);
            dataRow.createCell(3).setCellValue(notice.infringement_notice_officer_name);
            dataRow.createCell(4).setCellValue(notice.infringement_notice_charge_code + "");
            dataRow.createCell(5).setCellValue(notice.infringement_notice_status);
            dataRow.createCell(7).setCellValue(notice.infringement_notice_captured_by_name);
            dataRow.createCell(8).setCellValue(notice.infringement_notice_id_number);
            dataRow.createCell(9).setCellValue(notice.infringement_notice_final_amount);

            if (notice.infringement_notice_offence_date != null) {
                HSSFCell dateCell = dataRow.createCell(6);
                dateCell.setCellValue(Date.valueOf(notice.infringement_notice_offence_date));
                dateCell.setCellStyle(dateCellStyle);
            }
            dataRowIndex++;
            total += notice.infringement_notice_final_amount;
        }
        HSSFRow dataRow = sheet.createRow(dataRowIndex);
        dataRow.createCell(9).setCellValue(total);

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        return pdfService.downloadNoticeExcel(workbook, notices.get(0).infringement_notice_client_id,"notice");

    }

    @GetMapping("/downloadExcel")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        // Create a new Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SampleSheet");

        // Create sample data (you can replace this with your own data)
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Age");
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Meduim");
        row.createCell(1).setCellValue(30);

        // Write the workbook to a ByteArrayOutputStream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "sample.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream.toByteArray());
    }

    @GetMapping("/sendWhatsAppMessage")
    public String sendWhatsAppMessage(@RequestParam String phoneNumber, @RequestParam String message) {
        return twilioService.sendWhatsAppMessage(phoneNumber, message);
    }

    @GetMapping("/sendSimpleSMS")
    public String sendSimpleSMS(@RequestParam String phoneNumber, @RequestParam String message) {
        EmailServiceImpl emailService = new EmailServiceImpl();
        return emailService.sendSimpleSMS(phoneNumber, message);
    }

    @GetMapping("/checkBalanceSMS")
    public String checkBalanceSMS() {
        EmailServiceImpl emailService = new EmailServiceImpl();
        return emailService.checkBalanceSMS();
    }

    @GetMapping("/sendToDevice")
    public String sendToDevice(String token, String title, String body) throws FirebaseMessagingException {
        return pushNotificationService.sendNotificationToDevice(token, title, body);
    }

    @GetMapping("/sendToTopic")
    public String sendToTopic(String topic, String title, String body) throws FirebaseMessagingException {
        return pushNotificationService.sendNotificationToTopic(topic, title, body);
    }

}
