package com.mncedicy.stims.api.Controller;

import com.mncedicy.stims.api.Classes.*;
import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import com.mncedicy.stims.api.Services.EmailServiceImpl;
import com.mncedicy.stims.api.Services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@SuppressWarnings("ALL")
//@CrossOrigin(origins = "http://localhost:59536")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("management")
public class ManagementController {

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
    private InvoiceRepo invoiceRepo;
    @Autowired
    private InvoiceItemRepo invoiceItemRepo;


    @Autowired
    private CourtRollRepo courtRollRepo;
    @Autowired
    private CourtRollItemRepo courtRollItemRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final PdfService pdfService;
    public ManagementController(PdfService pdfService) {
        this.pdfService = pdfService;
    }


    @GetMapping(value = "/welcome")
    public String getPage(){
        return "Welcome";
    }


    @GetMapping(value = "/getInfringements")
    @ResponseBody
    public List<InfringementData> getInfringements(@RequestParam int client_id,@RequestParam String search_with,@RequestParam String search_keyword){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices = new ArrayList<>();
        notices = switch (search_with) {
            case "infringement_notice_reference" -> noticeRepo.findByClientReference(client_id, search_keyword);
            case "infringement_notice_id_number" -> noticeRepo.findByClientIdNumber(client_id, search_keyword);
            case "infringement_notice_registration" -> noticeRepo.findByClientRegistration(client_id, search_keyword);
            default -> notices;
        };
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringement.historyList = historyRepo.findByReference(notice.infringement_notice_id,"Infringement");
            List<invoice> invoices = invoiceRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringement.invoiceData = new InvoiceData();
            infringement.invoiceData.invoice = (!invoices.isEmpty())?invoices.get(0):null;
            infringement.invoiceData.invoice_items = invoiceItemRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringements.add(infringement);
        }
        return infringements;
    }


    @GetMapping(value = "/getInfringementsAccessStatus")
    @ResponseBody
    public List<InfringementData> getInfringementsAccessStatus(@RequestParam int client_id,@RequestParam String search_with,@RequestParam String search_keyword,@RequestParam String access_status){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices = new ArrayList<>();
        notices = switch (search_with) {
            case "infringement_notice_reference" -> noticeRepo.findByClientReferenceAccessStatus(client_id, search_keyword,access_status);
            case "infringement_notice_id_number" -> noticeRepo.findByClientIdNumberAccessStatus(client_id, search_keyword,access_status);
            case "infringement_notice_registration" -> noticeRepo.findByClientRegistrationAccessStatus(client_id, search_keyword,access_status);
            default -> notices;
        };
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringement.historyList = historyRepo.findByReference(notice.infringement_notice_id,"Infringement");
            List<invoice> invoices = invoiceRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringement.invoiceData = new InvoiceData();
            infringement.invoiceData.invoice = (!invoices.isEmpty())?invoices.get(0):null;
            infringement.invoiceData.invoice_items = invoiceItemRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringements.add(infringement);
        }
        return infringements;
    }






    @GetMapping(value = "/getInfringementsSearch")
    @ResponseBody
    public List<InfringementData> getInfringementsSearch(@RequestParam int client_id,@RequestParam String search_keyword){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices = noticeRepo.findByClientAll(client_id,search_keyword);
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringement.historyList = historyRepo.findByReference(notice.infringement_notice_id,"Infringement");
            List<invoice> invoices = invoiceRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringement.invoiceData = new InvoiceData();
            infringement.invoiceData.invoice = (!invoices.isEmpty())?invoices.get(0):null;
            infringement.invoiceData.invoice_items = invoiceItemRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringements.add(infringement);
        }
        return infringements;
    }

    @GetMapping(value = "/getInfringementsSearchLight")
    @ResponseBody
    public List<infringement_notice> getInfringementsSearchLight(@RequestParam int client_id,@RequestParam String search_keyword){
        return noticeRepo.findByClientAllOpen(client_id,search_keyword);
    }

    @GetMapping(value = "/getInfringementsByBookId")
    @ResponseBody
    public List<infringement_notice> getInfringementsByBookId(@RequestParam int book_id){
        return noticeRepo.findByBookId(book_id);
    }

    @GetMapping(value = "/getInfringementData")
    @ResponseBody
    public List<InfringementData> getInfringementData(@RequestParam long infringement_notice_id){
        List<InfringementData> infringements = new ArrayList<>();
        List<infringement_notice> notices = noticeRepo.findById(infringement_notice_id).stream().toList();
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringements.add(infringement);
        }
        return infringements;
    }



    @GetMapping(value = "/getChargeCodeSearch")
    @ResponseBody
    public List<charge_code> getChargeCodeSearch(@RequestParam int client_id,@RequestParam String search_keyword){
        return chargeCodeRepo.findByClientIdSearch(client_id,search_keyword);
    }


    @PutMapping(value = "/reduceNotice/{id}")
    public Response reduceNotice(@PathVariable long id,@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice updateNotice= noticeRepo.findById(id).get();
            updateNotice.infringement_notice_reduce_status = "Reduced";
            updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
            updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
           String current_amount = updateNotice.infringement_notice_final_amount+"";
            updateNotice.infringement_notice_final_amount = notice.infringement_notice_final_amount;
            updateNotice.infringement_notice_last_update = LocalDateTime.now();
            updateNotice = noticeRepo.save(updateNotice);



            history history = new history();
            history.history_reference_id = id;
            history.history_reference_type = "Infringement";
            history.history_value = notice.infringement_notice_holder_value;
            history.history_value1 = notice.infringement_notice_holder_value1;
            history.history_value2 = current_amount.equals("0.0") ?"NAG":current_amount;
            history.history_value3 = notice.infringement_notice_final_amount+"";
            history.history_value8 = notice.infringement_notice_reference;
            history.history_client_id = notice.infringement_notice_client_id;
            history.history_status = "New";
            history.history_action_by = notice.infringement_notice_status_updated_by;
            history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
            history.history_action = "Notice Reduced";
            historyRepo.save(history);

            response.setData(updateNotice);
            response.setMessage("Successfully Reduced");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/withdrawNotice/{id}")
    public Response withdrawNotice(@PathVariable long id,@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice updateNotice= noticeRepo.findById(id).get();
            updateNotice.infringement_notice_withdraw_status = "Withdrawn";
            updateNotice.infringement_notice_status = "Withdrawn";
            updateNotice.infringement_notice_access_status = "Closed";
            updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
            updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
            updateNotice.infringement_notice_last_update = LocalDateTime.now();
            updateNotice = noticeRepo.save(updateNotice);

            history history = new history();
            history.history_reference_id = id;
            history.history_reference_type = "Infringement";
            history.history_value = notice.infringement_notice_holder_value;
            history.history_value1 = notice.infringement_notice_holder_value1;
            history.history_value8 = notice.infringement_notice_reference;
            history.history_client_id = notice.infringement_notice_client_id;
            history.history_status = "New";
            history.history_action_by = notice.infringement_notice_status_updated_by;
            history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
            history.history_action = "Notice Withdrawn";
            historyRepo.save(history);

            response.setData(updateNotice);
            response.setMessage("Successfully Withdrawn");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/warrantNotice/{id}")
    public Response warrantNotice(@PathVariable long id,@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice updateNotice= noticeRepo.findById(id).get();
            updateNotice.infringement_notice_warrant_status = "Warrant";
            updateNotice.infringement_notice_status = "Warrant";
            updateNotice.infringement_notice_letter_status = "Warrant Letter";
            updateNotice.infringement_notice_letter_status_date = LocalDate.now();
            updateNotice.infringement_notice_final_amount = notice.infringement_notice_final_amount;
            updateNotice.infringement_notice_warrant_amount = notice.infringement_notice_warrant_amount;
            updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
            updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
            updateNotice.infringement_notice_last_update = LocalDateTime.now();
            updateNotice = noticeRepo.save(updateNotice);


            history history = new history();
            history.history_reference_id = id;
            history.history_reference_type = "Infringement";
            history.history_value = notice.infringement_notice_holder_value;
            history.history_value1 = notice.infringement_notice_holder_value1;
            history.history_value2 = notice.infringement_notice_warrant_amount+"";
            history.history_value3 = notice.infringement_notice_final_amount+"";
            history.history_value8 = notice.infringement_notice_reference;
            history.history_client_id = notice.infringement_notice_client_id;
            history.history_status = "New";
            history.history_action_by = notice.infringement_notice_status_updated_by;
            history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
            history.history_action = "Warrant Issued";
            historyRepo.save(history);

            response.setData(updateNotice);
            response.setMessage("Successfully Withdrawn");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/postponeNotice/{id}")
    public Response postponeNotice(@PathVariable long id,@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice updateNotice= noticeRepo.findById(id).get();
            updateNotice.infringement_notice_postpone_status = "Postponed";
            updateNotice.infringement_notice_court_date = notice.infringement_notice_court_date;
            updateNotice.infringement_notice_payment_due_date = notice.infringement_notice_payment_due_date;
            updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
            updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
            updateNotice.infringement_notice_last_update = LocalDateTime.now();
            updateNotice = noticeRepo.save(updateNotice);


            history history = new history();
            history.history_reference_id = id;
            history.history_reference_type = "Infringement";
            history.history_value = notice.infringement_notice_holder_value;
            history.history_value1 = notice.infringement_notice_holder_value1;
            history.history_value8 = notice.infringement_notice_reference;
            history.history_client_id = notice.infringement_notice_client_id;
            history.history_status = "New";
            history.history_action_by = notice.infringement_notice_status_updated_by;
            history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
            history.history_action = "Notice Postponed";
            historyRepo.save(history);

            response.setData(updateNotice);
            response.setMessage("Successfully Withdrawn");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PutMapping(value = "/resultCapturedNotice/{id}")
    public Response resultCapturedNotice(@PathVariable long id,@RequestBody infringement_notice notice){

        Response response = new Response();
        response.setStatus("Error");
        try {
            infringement_notice updateNotice= noticeRepo.findById(id).get();
            updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
            updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
            updateNotice.infringement_notice_last_update = LocalDateTime.now();
            updateNotice = noticeRepo.save(updateNotice);


            history history = new history();
            history.history_reference_id = id;
            history.history_reference_type = "Infringement";
            history.history_value = notice.infringement_notice_holder_value;
            history.history_value1 = notice.infringement_notice_holder_value1;
            history.history_value8 = notice.infringement_notice_reference;
            history.history_client_id = notice.infringement_notice_client_id;
            history.history_status = "New";
            history.history_action_by = notice.infringement_notice_status_updated_by;
            history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
            history.history_action = "Court Results Captured";
            historyRepo.save(history);

            response.setData(updateNotice);
            response.setMessage("Successfully Withdrawn");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PostMapping(value = "/payNotice")
    public Response payNotice(@RequestBody List<infringement_notice> notices){

        Response response = new Response();
        response.setStatus("Error");
        try {

            invoice invoice = new invoice();
            invoice.invoice_amount= notices.get(0).infringement_notice_holder_value_double;
            invoice.invoice_client_id = notices.get(0).infringement_notice_client_id;
            invoice.invoice_name_from = notices.get(0).infringement_notice_holder_value2;
            invoice.invoice_name_to = notices.get(0).infringement_notice_holder_value3;
            invoice.invoice_created_by = notices.get(0).infringement_notice_status_updated_by;
            invoice.invoice_created_by_name = notices.get(0).infringement_notice_status_updated_by_name;
            invoice.invoice_payment_date = LocalDate.parse(notices.get(0).infringement_notice_holder_value1);
            invoice.invoice_payment_type = notices.get(0).infringement_notice_holder_value;
            invoice.invoice_records = notices.size();
            invoice = invoiceRepo.save(invoice);


            for(infringement_notice notice:notices){
                invoice_item invoice_item = new invoice_item();
                invoice_item.invoice_item_invoice_number = invoice.invoice_number;
                invoice_item.invoice_item_amount = notice.infringement_notice_final_amount;
                invoice_item.invoice_item_client_id = notice.infringement_notice_client_id;
                invoice_item.invoice_item_created_by = notice.infringement_notice_status_updated_by;
                invoice_item.invoice_item_created_by_name = notice.infringement_notice_status_updated_by_name;
                invoice_item.invoice_item_id_number = notice.infringement_notice_id_number;
                invoice_item.invoice_item_payment_date = invoice.invoice_payment_date;
                invoice_item.invoice_item_payment_type = invoice.invoice_payment_type;
                invoice_item.invoice_item_reference = notice.infringement_notice_reference;
                invoice_item.invoice_item_registration = notice.infringement_notice_registration;
                invoiceItemRepo.save(invoice_item);

                infringement_notice updateNotice= noticeRepo.findById(notice.infringement_notice_id).get();
                updateNotice.infringement_notice_invoice_number = invoice.invoice_number;
                updateNotice.infringement_notice_status = "Paid";
                updateNotice.infringement_notice_access_status = "Closed";
                updateNotice.infringement_notice_last_update = LocalDateTime.now();
                updateNotice.infringement_notice_status_updated_by_name = notice.infringement_notice_status_updated_by_name;
                updateNotice.infringement_notice_status_updated_by = notice.infringement_notice_status_updated_by;
                updateNotice.infringement_notice_payment_status =  "Paid";
                noticeRepo.save(updateNotice);



                history history = new history();
                history.history_reference_id = updateNotice.infringement_notice_id;
                history.history_reference_type = "Infringement";
                history.history_value = notice.infringement_notice_holder_value;
                history.history_value1 = notice.infringement_notice_holder_value1;
                history.history_value2 = invoice.invoice_number+"";
                history.history_value3 = notice.infringement_notice_holder_value_double+"";
                history.history_value4 = notice.infringement_notice_final_amount+"";
                history.history_value8 = notice.infringement_notice_reference;
                history.history_client_id = notice.infringement_notice_client_id;
                history.history_status = "New";
                history.history_action_by = notice.infringement_notice_status_updated_by;
                history.history_action_by_name = notice.infringement_notice_status_updated_by_name;
                history.history_action = "Notice Payment";
                historyRepo.save(history);
            }
            response.setData(invoice);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @GetMapping(value = "/getInvoice")
    public ResponseEntity<byte[]> getInvoice(@RequestParam long invoice_number) {
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.invoice = invoiceRepo.findById(invoice_number).get();
        invoiceData.invoice_items = invoiceItemRepo.findByInvoiceNumber(invoice_number);
        return pdfService.downloadInvoiceResponseEntity(invoiceData);
    }


    @GetMapping(value = "/getCourtRolls")
    @ResponseBody
    public List<court_roll> getCourtRolls(@RequestParam int client_id){
        return courtRollRepo.findByClientId(client_id);
    }

    @GetMapping(value = "/getCourtRollsItems")
    @ResponseBody
    public List<court_roll_item> getCourtRollsItems(@RequestParam long court_roll_id){
        return courtRollItemRepo.findByCourtRollId(court_roll_id);
    }

    @PostMapping(value = "/createCourRoll")
    public Response createCourRoll(@RequestBody court_roll court_roll){

        Response response = new Response();
        response.setStatus("Error");
        try {
            List<infringement_notice> notices = noticeRepo.findTodayCourtRoll(court_roll.court_roll_court_id,court_roll.court_roll_infringement_type);
            if(!notices.isEmpty()){
                court_roll.court_roll_records = notices.size();
                court_roll.court_roll_court_date = LocalDate.now();
                court_roll = courtRollRepo.save(court_roll);

                for(infringement_notice notice:notices){
                    charge_code charge_code = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code).get(0);
                    String descr = charge_code.charge_code_description;
                    descr = descr.replace("[VEHMAKE]",notice.infringement_notice_vehicle_make);
                    descr = descr.replace("[VEHMODEL]",notice.infringement_notice_vehicle_model);
                    descr = descr.replace("[VEHREG]",notice.infringement_notice_registration);

                    infringement_infringer infringer = infringerRepo.findByNoticeRef(notice.infringement_notice_reference).get(0);

                    court_roll_item item = new court_roll_item();
                    item.court_roll_item_charge_code = charge_code.charge_code;
                    item.court_roll_item_charge_description = descr;
                    item.court_roll_item_client_id = court_roll.court_roll_client_id;
                    item.court_roll_item_court_date = LocalDate.now();
                    item.court_roll_item_charge_regulation = charge_code.charge_code_regulation;
                    item.court_roll_item_court_name = court_roll.court_roll_court_name;
                    item.court_roll_item_court_roll_id = court_roll.court_roll_id;
                    item.court_roll_item_created_by = court_roll.court_roll_created_by;
                    item.court_roll_item_created_by_name = court_roll.court_roll_created_by_name;
                    item.court_roll_item_fine_amount = charge_code.charge_code_fine_amount;
                    item.court_roll_item_id_number = notice.infringement_notice_id_number;
                    item.court_roll_item_infringement_type = court_roll.court_roll_infringement_type;
                    item.court_roll_item_notice_reference = notice.infringement_notice_reference;
                    item.court_roll_item_offence_date = notice.infringement_notice_offence_date;
                    item.court_roll_item_offence_time = notice.infringement_notice_offence_time;
                    item.court_roll_item_officer_id = notice.infringement_notice_officer_id;
                    item.court_roll_item_officer_name = notice.infringement_notice_officer_name;
                    item.court_roll_item_registration = notice.infringement_notice_registration;
                    item.court_roll_item_name = infringer.infringement_infringer_name+" "+infringer.infringement_infringer_surname;
                    item = courtRollItemRepo.save(item);

                    notice.infringement_notice_court_roll_printed =  "yes";
                    noticeRepo.save(notice);

                    history history = new history();
                    history.history_reference_id = notice.infringement_notice_id;
                    history.history_reference_type = "Infringement";
                    history.history_value = court_roll.court_roll_court_name;
                    history.history_value1 = court_roll.court_roll_court_date+"";
                    history.history_value2 = court_roll.court_roll_infringement_type;
                    history.history_value3 = notice.infringement_notice_registration;
                    history.history_value4 = charge_code.charge_code+"";
                    history.history_value5 = court_roll.court_roll_id+"";
                    history.history_value6 = item.court_roll_item_id+"";
                    history.history_value8 = notice.infringement_notice_reference;
                    history.history_client_id = court_roll.court_roll_client_id;
                    history.history_action_by = court_roll.court_roll_created_by;
                    history.history_action_by_name = court_roll.court_roll_created_by_name;
                    history.history_action = "Court Roll Printed";
                    historyRepo.save(history);
                }

                response.setData(court_roll);
                response.setMessage("Successfully Saved");
                response.setStatus("Success");
            }
            else {
                response.setMessage("No infringements available to print");
            }

        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @PutMapping(value = "/updateCourtRollItem/{id}")
    public Response updateCourtRollItem(@PathVariable long id,@RequestBody court_roll_item item){

        Response response = new Response();
        response.setStatus("Error");
        try {
            court_roll_item roll_item= courtRollItemRepo.findById(id).get();
            roll_item.court_roll_item_results = item.court_roll_item_results;
            roll_item.court_roll_item_results_new_court_date = item.court_roll_item_results_new_court_date;
            roll_item.court_roll_item_results_fine_amount = item.court_roll_item_results_fine_amount;
            roll_item.court_roll_item_results_invoice_number = item.court_roll_item_results_invoice_number;
            roll_item.court_roll_item_results_reduced_to_amount = item.court_roll_item_results_reduced_to_amount;
            roll_item.court_roll_item_results_description = item.court_roll_item_results_description;
            roll_item.court_roll_item_status = item.court_roll_item_status;
            roll_item.court_roll_item_results_captured_by_name =item.court_roll_item_results_captured_by_name;
            roll_item.court_roll_item_results_captured_by = item.court_roll_item_results_captured_by;
            roll_item = courtRollItemRepo.save(roll_item);

            court_roll roll= courtRollRepo.findById(roll_item.court_roll_item_court_roll_id).get();
            roll.court_roll_status = "Captured";
            roll.court_roll_captured_records = courtRollItemRepo.findByCourtRollIdCaptured(roll.court_roll_id).size();
            roll = courtRollRepo.save(roll);

            response.setData(roll_item);
            response.setExtra(roll.court_roll_captured_records+"");
            response.setMessage("Successfully Updated");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PutMapping(value = "/saveCourtResults/{id}")
    public Response saveCourtResults(@PathVariable long id,@RequestBody court_roll roll){

        Response response = new Response();
        response.setStatus("Error");
        try {
            Client client = clientRepo.findById(roll.court_roll_client_id).get();

            court_roll court_roll= courtRollRepo.findById(id).get();
            court_roll.court_roll_court_public_prosecutor = roll.court_roll_court_public_prosecutor;
            court_roll.court_roll_court_preciding_officer = roll.court_roll_court_preciding_officer;
            court_roll.court_roll_court_clerk = roll.court_roll_court_clerk;
            court_roll.court_roll_status = roll.court_roll_status;
            court_roll.court_roll_results_captured_by_name = roll.court_roll_results_captured_by_name;
            court_roll.court_roll_results_captured_by = roll.court_roll_results_captured_by;
            court_roll.court_roll_results_date = LocalDateTime.now();
            court_roll = courtRollRepo.save(court_roll);

            List<court_roll_item> roll_items = courtRollItemRepo.findByCourtRollId(court_roll.court_roll_id);
            for(court_roll_item item:roll_items){
                infringement_notice notice = noticeRepo.findByClientReference(item.court_roll_item_client_id,item.court_roll_item_notice_reference).get(0);
                notice.infringement_notice_status_updated_by = roll.court_roll_results_captured_by;
                notice.infringement_notice_status_updated_by_name = roll.court_roll_results_captured_by_name;
                notice.infringement_notice_holder_value = item.court_roll_item_results;
                notice.infringement_notice_holder_value1 = item.court_roll_item_results_description;

                if(item.court_roll_item_results.equals("Warrant issued immediately")){
                    notice.infringement_notice_final_amount += client.client_warrant_amount;
                    notice.infringement_notice_warrant_amount = client.client_warrant_amount;
                    warrantNotice(notice.infringement_notice_id,notice);
                }
                else if(item.court_roll_item_results.equals("Postponed: Accuse warned")){
                    notice.infringement_notice_court_date = item.court_roll_item_results_new_court_date;
                    notice.infringement_notice_payment_due_date = item.court_roll_item_results_new_court_date.isBefore(notice.infringement_notice_payment_due_date)?item.court_roll_item_results_new_court_date:notice.infringement_notice_payment_due_date;
                    postponeNotice(notice.infringement_notice_id,notice);
                }
                else if(item.court_roll_item_results.equals("Reduced by prosecutor")){
                    notice.infringement_notice_final_amount = item.court_roll_item_results_reduced_to_amount;
                    reduceNotice(notice.infringement_notice_id,notice);
                }
                else if(item.court_roll_item_results.equals("Withdrawn by prosecutor")){
                    withdrawNotice(notice.infringement_notice_id,notice);
                }
                else if(item.court_roll_item_results.equals("Admission of guilt paid")){
                    notice.infringement_notice_holder_value = "Cash";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    notice.infringement_notice_holder_value1 = item.court_roll_item_court_date.format(formatter);
                    notice.infringement_notice_holder_value2 = roll.court_roll_authority_name;
                    notice.infringement_notice_holder_value3 = item.court_roll_item_name;
                    notice.infringement_notice_holder_value_double = item.court_roll_item_results_fine_amount;
                    payNotice(List.of(notice));
                }
                else {
                    resultCapturedNotice(notice.infringement_notice_id,notice);
                }
                item.court_roll_item_status = roll.court_roll_status;
                courtRollItemRepo.save(item);
            }


            response.setData(court_roll);
            response.setMessage("Successfully Updated");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @GetMapping(value = "/getPrintCourtRoll")
    public ResponseEntity<byte[]> getPrintCourtRoll(@RequestParam long court_roll_id,@RequestParam String type) {
        CourtRollData courtRollData = new CourtRollData();
        courtRollData.court_roll = courtRollRepo.findById(court_roll_id).get();
        courtRollData.court_roll_items = courtRollItemRepo.findByCourtRollId(court_roll_id);
        return pdfService.downloadCourtRollResponseEntity(courtRollData,type);
    }


    public void runEvey1Minutes() {
        for(court_roll_item item:courtRollItemRepo.findAll()) {
            System.out.println("Current time is :: " + item.court_roll_item_notice_reference);
            System.out.println(passwordEncoder.encode("00038150"));
        }
    }


    @GetMapping(value = "/sendEmail")
    public void sendEmail() {
        EmailServiceImpl emailService = new EmailServiceImpl();
        //emailService.sendSimpleMessage("mkhonzenimkhonzeni@gmail.com","test","Hi there");

        emailService.sendMessageWithInputStreamAttachment(
                new String[]{"mkhonzenimkhonzeni@gmail.com"},
                "Subject",
                "Body of the email",
                new String[]{"C:\\htmltopdf\\payment_receipt_10007.pdf", "C:\\htmltopdf\\report_out.pdf"});

    }




    @GetMapping(value = "/getletter")
    @ResponseBody
    public List<InfringementData> getletter(@RequestParam int client_id,@RequestParam String search_with,@RequestParam String date_from,@RequestParam String date_to){
        List<InfringementData> infringements = new ArrayList<>();

        List<infringement_notice> notices= noticeRepo.findLetterByDateRange(client_id,search_with,LocalDate.parse(date_from),LocalDate.parse(date_to));
        for(infringement_notice notice : notices){
            InfringementData infringement = new InfringementData();
            infringement.notice = notice;
            List<infringement_infringer> infringers = infringerRepo.findByNoticeIdAndStatus(notice.infringement_notice_id,"active");
            infringement.infringer = !infringers.isEmpty()?infringers.get(0):new infringement_infringer();
            List<charge_code> charge_codes = chargeCodeRepo.findByChargeCode(notice.infringement_notice_charge_code);
            infringement.charge_code = !charge_codes.isEmpty()?charge_codes.get(0):new charge_code();
            infringement.historyList = historyRepo.findByReference(notice.infringement_notice_id,"Infringement");
            List<invoice> invoices = invoiceRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringement.invoiceData = new InvoiceData();
            infringement.invoiceData.invoice = (!invoices.isEmpty())?invoices.get(0):null;
            infringement.invoiceData.invoice_items = invoiceItemRepo.findByInvoiceNumber(notice.infringement_notice_invoice_number);
            infringements.add(infringement);
        }
        return infringements;
    }

    @PostMapping(value = "/getletterPrint/{id}")
    public ResponseEntity<byte[]> getletterPrint(@PathVariable int id,@RequestParam String letter_status,@RequestBody List<InfringementData> infringementDataList) {
        Client client= clientRepo.findById(id).get();
        return pdfService.downloadLegalLetterResponseEntity(infringementDataList,letter_status, client);
    }


    @PostMapping(value = "/getWarrant")
    public Response getWarrant(@RequestBody InfringementData infringementData) {
        Response response = new Response();
        response.setStatus("Success");
        Client client= clientRepo.findById(infringementData.notice.infringement_notice_client_id).get();
        ResponseEntity<byte[]> b =pdfService.downloadLegalLetterResponseEntity(Arrays.stream(new InfringementData[]{infringementData}).toList(),"Warrant Letter", client);
        Base64.Encoder encoder = Base64.getEncoder();
        response.setData(encoder.encodeToString(b.getBody()));
        return response;
    }



    @GetMapping(value = "/printOutstanding")
    @ResponseBody
    public ResponseEntity<byte[]> printOutstanding(@RequestParam int client_id,@RequestParam String infringement_notice_id_number){
        List<infringement_notice> notices= noticeRepo.findByClientIdNumberOpen(client_id,infringement_notice_id_number);
        List<String> registrations = new ArrayList<>();
        List<List<infringement_notice>> noticeList = new ArrayList<>();
        String name = "",id_number = "";
        double total=0,totalWarrant=0;
        int count=0,countWarrant=0;
        for(infringement_notice notice:notices){
            int index = registrations.indexOf(notice.infringement_notice_registration);
            if(index>=0)
                noticeList.get(index).add(notice);
            else {
                registrations.add(notice.infringement_notice_registration);
                noticeList.add(new ArrayList<>(List.of(notice)));
            }
            count++;
            total+=notice.infringement_notice_final_amount;
            name = notice.infringement_notice_name;
            id_number = notice.infringement_notice_id_number;
            if(notice.infringement_notice_status.equals("Warrant")){
                countWarrant++;
                totalWarrant+=notice.infringement_notice_final_amount;
            }
        }

        Map<String, Object> templateVariables = Map.of(
                "noticeList", noticeList,
                "registrations", registrations,
                "name", name,
                "id_number", id_number,
                "total", total,
                "totalWarrant", totalWarrant,
                "count", count,
                "countWarrant", countWarrant,
                "client_id", client_id);

        return pdfService.downloadPrintOutstanding(templateVariables);

    }













}
