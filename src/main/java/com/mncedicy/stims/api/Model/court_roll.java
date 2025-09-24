package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class court_roll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long court_roll_id;
    public String court_roll_court_name;
    public int court_roll_court_id;
    public String court_roll_authority_name;
    public int court_roll_client_id;
    public String court_roll_infringement_type;
    public LocalDate court_roll_court_date;
    public int court_roll_records;
    public long court_roll_created_by;
    public String court_roll_created_by_name;
    public String court_roll_status = "Printed";
    public LocalDateTime court_roll_timestamp= LocalDateTime.now();
    public String court_roll_description;
    public int court_roll_print_count=1;
    public String court_roll_court_address;
    public String court_roll_court_preciding_officer;
    public String court_roll_court_public_prosecutor;
    public String court_roll_court_interpreter;
    public String court_roll_court_clerk;
    public int court_roll_captured_records;
    public long court_roll_results_captured_by;
    public String court_roll_results_captured_by_name;
    public LocalDateTime court_roll_results_date;
}



