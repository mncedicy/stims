package com.mncedicy.stims.api.Controller;

import com.google.gson.Gson;
import com.mncedicy.stims.api.Classes.*;
import com.mncedicy.stims.api.Classes.IKhokha.PayRequest;
import com.mncedicy.stims.api.Classes.IKhokha.PayResponse;
import com.mncedicy.stims.api.Model.*;
import com.mncedicy.stims.api.Repo.*;
import com.mncedicy.stims.api.Services.EmailServiceImpl;
import com.mncedicy.stims.api.Services.IKhokhaPayService;
import com.mncedicy.stims.api.Services.TwilioService;
import jakarta.mail.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
//@CrossOrigin(origins = "http://localhost:59536")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("configuration")
public class ConfigurationController {

    @Autowired
    private ChargeCodeRepo chargeCodeRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private RuleRepo ruleRepo;
    @Autowired
    private RuleReceiverRepo ruleReceiverRepo;

    @Autowired
    private NoticeRepo noticeRepo;
    @Autowired
    private IkhokhaPaymentRepo ikhokhaPaymentRepo;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private IKhokhaPayService iKhokhaPayService;

    @GetMapping(value = "/welcome")
    public String getPage(){
        return "Welcome";
    }

    @GetMapping(value = "/getChargeCodes")
    @ResponseBody
    public List<charge_code> getChargeCodes(@RequestParam int client_id){
        List<charge_code> charge_codes= chargeCodeRepo.findByClientId(client_id);
        return charge_codes;
    }


    @GetMapping(value = "/getAutomations")
    @ResponseBody
    public List<RuleData> getAutomations(@RequestParam int client_id){
        List<rule> rules= ruleRepo.findByClientId(client_id);
        List<RuleData> ruleDataArrayList = new ArrayList<>();
        for(rule rule :rules){
            RuleData ruleData = new RuleData();
            ruleData.rule= rule;
            ruleData.rule_receivers = ruleReceiverRepo.findByRuleId(rule.rule_id);
            ruleDataArrayList.add(ruleData);
        }
        return ruleDataArrayList;
    }


    @GetMapping(value = "/runAutomations")
    @ResponseBody
    public Response runAutomations(){
        Response response = new Response();
        response.setStatus("Error");

        List<rule> rules= ruleRepo.findAllActive();
        List<RuleNoticesData> ruleNoticesDataList = new ArrayList<>();
        for(rule rule :rules){
            RuleNoticesData ruleNoticesData = new RuleNoticesData();
            if(rule.rule_when.equals("After Capture Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByCaptureDate(rule.rule_client_id, LocalDate.now().minusDays(rule.rule_days_repeats)));
            else if(rule.rule_when.equals("After Offence Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByOffenceDate(rule.rule_client_id, LocalDate.now().minusDays(rule.rule_days_repeats)));
            else if(rule.rule_when.equals("Before Court Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByCourtDate(rule.rule_client_id, LocalDate.now().plusDays(rule.rule_days_repeats)));
            else if(rule.rule_when.equals("After Court Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByCourtDate(rule.rule_client_id, LocalDate.now().minusDays(rule.rule_days_repeats)));
            else if(rule.rule_when.equals("Before Payment Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByPaymentDate(rule.rule_client_id, LocalDate.now().plusDays(rule.rule_days_repeats)));
            else if(rule.rule_when.equals("After Payment Date"))
                ruleNoticesData = new RuleNoticesData(rule,noticeRepo.findByPaymentDate(rule.rule_client_id, LocalDate.now().minusDays(rule.rule_days_repeats)));


            if(ruleNoticesData.notices.size()>0)
                ruleNoticesDataList.add(ruleNoticesData);
        }


        for(RuleNoticesData data :ruleNoticesDataList){

            for(infringement_notice notice :data.notices){
                if(!notice.infringement_notice_cellphone.isEmpty()){
                    if(data.rule.rule_trigger.equals("Whatsapp"))
                        sendWhatsAppMessage(notice.infringement_notice_cellphone,replaceVariables(notice,data.rule.rule_message));
                    else if(data.rule.rule_trigger.equals("SMS"))
                        sendSimpleSMS(notice.infringement_notice_cellphone,replaceVariables(notice,data.rule.rule_message));

                }
            }

        }


        response.setData(ruleNoticesDataList);
        response.setMessage("Successfully");
        response.setStatus("Success");
        return response;
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


    @PostMapping(value = "/payment1")
    public Response payment1(@RequestBody PayRequest data) throws URISyntaxException {
        Response response = new Response();
        System.out.println(data);
        System.out.println(data.externalEntityID);
        response.setData(data);
        String requestUrl = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        URI uri = new URI(requestUrl);
        response.setMessage(uri.resolve("/").toString());
        response.setStatus("Success");
        return response;

    }


    @PostMapping(value = "/payment")
    public Response payment(@RequestBody PayRequest payRequest)  {
        Response response = iKhokhaPayService.payment(payRequest);
        if(response.getStatus().equals("Success")){
            PayResponse payResponse = (PayResponse) response.getData();
            ikhokha_payment payment= new ikhokha_payment();
            payment.ikhokha_payment_amount = payRequest.amount;
            payment.ikhokha_payment_fine = ((double) payRequest.amount)/100;
            payment.ikhokha_payment_client_id = Integer.parseInt(payRequest.externalEntityID);
            payment.ikhokha_payment_description = payRequest.externalTransactionID;
            payment.ikhokha_payment_entity_id = payRequest.entityID;
            payment.ikhokha_payment_external_entity_id = payRequest.externalEntityID;
            payment.ikhokha_payment_external_transaction_id = payRequest.externalTransactionID;
            payment.ikhokha_payment_notice_reference = payRequest.externalTransactionID;
            payment.ikhokha_payment_status = "UNPAID";
            payment.ikhokha_payment_timestamp = LocalDateTime.now();
            payment.ikhokha_payment_paylink_id = payResponse.paylinkID;
            payment.ikhokha_payment_paylink_url = payResponse.paylinkUrl;
            payment.ikhokha_payment_response_code = payResponse.responseCode;
            payment.ikhokha_payment_message = payResponse.message;
            List<ikhokha_payment> payments = ikhokhaPaymentRepo.findByPayLinkId(payResponse.paylinkID);
            if(payments.isEmpty())
                payment = ikhokhaPaymentRepo.save(payment);
            response.setData(payment);
        }

        return response;
    }

    @GetMapping(value = "/getPaymentStatus")
    public Response getPaymentStatus(@RequestParam String paylinkId)  {

        return iKhokhaPayService.getPaymentStatus(paylinkId);

    }

    @GetMapping(value = "/getPaymentHistory")
    public Response getPaymentHistory(@RequestParam String startDate,@RequestParam String endDate)  {

        return iKhokhaPayService.getPaymentHistory(startDate,endDate);

    }

    @PostMapping("/payment/webhook/callback")
    public Object handleWebhook(@RequestBody PayResponse payload) {
        // Process the incoming webhook payload here
        System.out.println("Received webhook payload: " + payload);
        return payload;
    }





    @PostMapping(value = "/saveRule")
    public Response saveRule(@RequestBody RuleData data){
        Response response = new Response();
        EmailServiceImpl emailService = new EmailServiceImpl();
        response.setStatus("Error");
        try {

            if(data.rule.rule_id==0){
                List<rule> rules = ruleRepo.findByName(data.rule.rule_name);
                if(!rules.isEmpty()){
                    response.setMessage("Rule: "+data.rule.rule_name+" already added");
                    return response;
                }
                data.rule.rule_timestamp = LocalDateTime.now();
            }
            else{
                data.rule.rule_last_update = LocalDateTime.now();
            }

            data.rule = ruleRepo.save(data.rule);

            response.setData(data);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }


    public String replaceVariables(infringement_notice notice,String message){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        message = message.replace("{{Fine}}",notice.infringement_notice_final_amount+"");
        message = message.replace("{{OffenceDate}}",notice.infringement_notice_offence_date.format(formatter));
        message = message.replace("{{CourtDate}}",notice.infringement_notice_court_date.format(formatter));
        message = message.replace("{{CourtName}}",notice.infringement_notice_court_name+"");
        message = message.replace("{{Reference}}",notice.infringement_notice_reference+"");
        message = message.replace("{{Registration}}",notice.infringement_notice_registration+"");
        message = message.replace("{{Location}}",notice.infringement_notice_offence_location+"");
        message = message.replace("{{PaymentDate}}",notice.infringement_notice_payment_due_date.format(formatter));
        message = message.replace("{{IDNumber}}",notice.infringement_notice_id_number+"");
        message = message.replace("{{InvoiceNumber}}",notice.infringement_notice_invoice_number+"");
        message = message.replace("{{Charge}}",notice.infringement_notice_charge_description+"");
        message = message.replace("{{OffenderName}}",notice.infringement_notice_name+"");
        message = message.replace("{{ClientName}}",notice.infringement_notice_client_name+"");

        return  message;
    }





    @GetMapping(value = "/getChargeCode")
    @ResponseBody
    public List<charge_code> getChargeCode(@RequestParam int charge_code){
        List<charge_code> charge_codes= chargeCodeRepo.findByChargeCode(charge_code);
        return charge_codes;
    }


    @PostMapping(value = "/saveChargeFile")
    public Response saveChargeFile(@RequestBody List<charge_code> charge_codes){

        Response response = new Response();
        response.setStatus("Error");
        try {
            charge_codes = chargeCodeRepo.saveAll(charge_codes);
            response.setData(charge_codes);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;
    }




    @GetMapping(value = "/getClient")
    @ResponseBody
    public ClientData getClient(@RequestParam int client_id){
        ClientData clientData = new ClientData();
        Client client= clientRepo.findById(client_id).get();
        clientData.client = client;
        clientData.contact = contactRepo.findById(client.client_contact_id).get();
        return clientData;
    }


    @PostMapping(value = "/saveClient")
    public Response saveClient(@RequestBody ClientData data){
        Response response = new Response();
        response.setStatus("Error");
        try {
           Client client = clientRepo.findById(data.client.client_id).get();
            client.client_name = data.client.client_name;
            client.client_email = data.client.client_email;
            client.client_phone = data.client.client_phone;
            client.client_website = data.client.client_website;
            client.client_logo = data.client.client_logo;
            client.client_authority_code = data.client.client_authority_code;
            client.client_warrant_amount = data.client.client_warrant_amount;
            client.client_discount = data.client.client_discount;
            client.client_last_updated = LocalDateTime.now();
            client.client_updated_by = data.client.client_updated_by;
            client.client_updated_by_name = data.client.client_updated_by_name;
            client.client_fax = data.client.client_fax;
            client.client_hour_opening = data.client.client_hour_opening;
            client.client_hour_closing = data.client.client_hour_closing;
            client.client_bank_account_name = data.client.client_bank_account_name;
            client.client_bank_name = data.client.client_bank_name;
            client.client_bank_account_number = data.client.client_bank_account_number;
            client.client_bank_branch_code = data.client.client_bank_branch_code;
            client.client_bank_account_Type = data.client.client_bank_account_Type;
            client.client_courtesy_amount = data.client.client_courtesy_amount;
            client.client_enforcement_amount = data.client.client_enforcement_amount;
            client.client_alt_phone = data.client.client_alt_phone;
            client.client_discount_percentage = data.client.client_discount_percentage;
            client.client_discount_timeframe = data.client.client_discount_timeframe;
            client.client_courtesy = data.client.client_courtesy;
            client.client_courtesy_timeframe = data.client.client_courtesy_timeframe;
            client.client_enforcement = data.client.client_enforcement;
            client.client_enforcement_timeframe = data.client.client_enforcement_timeframe;
            client.client_warrant = data.client.client_warrant;
            client.client_warrant_timeframe = data.client.client_warrant_timeframe;

            data.client = clientRepo.save(client);

            Contact contact = contactRepo.findById(data.contact.contact_id).get();
            contact.contact_telephone = data.contact.contact_telephone;
            contact.contact_work_telephone = data.client.client_alt_phone;
            contact.contact_website = data.contact.contact_website;
            contact.contact_last_update = LocalDateTime.now();
            contact.contact_cellphone = data.contact.contact_cellphone;
            contact.contact_email_address = data.contact.contact_email_address;
            contact.contact_physical_address_street = data.contact.contact_physical_address_street;
            contact.contact_physical_address_suburb = data.contact.contact_physical_address_suburb;
            contact.contact_physical_address_city = data.contact.contact_physical_address_city;
            contact.contact_physical_address_code = data.contact.contact_physical_address_code;
            contact.contact_postal_address_street = data.contact.contact_postal_address_street;
            contact.contact_postal_address_suburb = data.contact.contact_postal_address_suburb;
            contact.contact_postal_address_city = data.contact.contact_postal_address_city;
            contact.contact_postal_address_code = data.contact.contact_postal_address_code;
            data.contact = contactRepo.save(contact);

            response.setData(data);
            response.setMessage("Successfully Saved");
            response.setStatus("Success");
        }catch (Exception e){
            response.setMessage(e.getMessage());
        }
        return response;

    }



}
