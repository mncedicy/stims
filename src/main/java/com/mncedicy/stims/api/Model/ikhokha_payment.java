package com.mncedicy.stims.api.Model;

import com.mncedicy.stims.api.Classes.IKhokha.Urls;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ikhokha_payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long ikhokha_payment_id;
    public String ikhokha_payment_entity_id;
    public String ikhokha_payment_external_entity_id;
    public String ikhokha_payment_paylink_url;
    public String ikhokha_payment_paylink_id;
    public int ikhokha_payment_amount;
    public double ikhokha_payment_fine;
    public String ikhokha_payment_currency= "ZAR";
    public String ikhokha_payment_requester_url;
    public String ikhokha_payment_description;
    public String ikhokha_payment_payment_reference;
    public String ikhokha_payment_mode= "live";
    public String ikhokha_payment_external_transaction_id;
    public String ikhokha_payment_callback_url;
    public String ikhokha_payment_success_url;
    public String ikhokha_payment_failure_url;
    public String ikhokha_payment_cancel_url;
    public String ikhokha_payment_response_code;
    public String ikhokha_payment_status;
    public String ikhokha_payment_message;
    public String ikhokha_payment_created_at;
    public LocalDateTime ikhokha_payment_last_update =LocalDateTime.now();
    public LocalDateTime ikhokha_payment_timestamp=LocalDateTime.now();
    public int ikhokha_payment_client_id;
    public String ikhokha_payment_notice_reference;
    public long ikhokha_payment_invoice_number;

}
