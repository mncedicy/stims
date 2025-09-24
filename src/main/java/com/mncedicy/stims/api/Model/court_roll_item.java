package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class court_roll_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long court_roll_item_id;
    public long court_roll_item_court_roll_id;
    public String court_roll_item_court_name;
    public int court_roll_item_client_id;
    public String court_roll_item_infringement_type;
    public LocalDate court_roll_item_court_date;
    public String court_roll_item_results;
    public String court_roll_item_results_description;
    public LocalDateTime court_roll_item_results_date;
    public long court_roll_item_results_captured_by;
    public String court_roll_item_results_captured_by_name;
    public long court_roll_item_results_invoice_number;
    public double court_roll_item_results_fine_amount;
    public LocalDate court_roll_item_results_new_court_date;
    public double court_roll_item_results_reduced_to_amount;
    public int court_roll_item_case_number;
    public String court_roll_item_notice_reference;
    public String court_roll_item_name;
    public String court_roll_item_id_number;
    public LocalDate court_roll_item_offence_date;
    public LocalDateTime court_roll_item_offence_time;
    public String court_roll_item_registration;
    public String court_roll_item_fine_amount;
    public int court_roll_item_charge_code;
    @Column(length = 5000)
    public String court_roll_item_charge_regulation;
    @Column(length = 5000)
    public String court_roll_item_charge_description;
    public String court_roll_item_officer_name;
    public long court_roll_item_officer_id;
    public long court_roll_item_created_by;
    public String court_roll_item_created_by_name;
    public String court_roll_item_status="Printed";
    public LocalDateTime court_roll_item_timestamp= LocalDateTime.now();
    public String court_roll_item_description;
}



