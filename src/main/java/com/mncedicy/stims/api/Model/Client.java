package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("ALL")
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int client_id;
    @Column
    public String client_name;
    public String client_authority_code;
    @Column
    public String client_address;
    @Column
    public String client_province;
    @Column
    public String client_region;
    @Column
    public String client_city;
    @Column
    public long client_contact_id;
    @Column
    public long client_updated_by;
    @Column
    public String client_updated_by_name;
    @Column
    public String client_email;
    @Column
    public String client_phone;
    @Column
    public String client_alt_phone;
    @Column
    public String client_website;
    @Column
    public String client_reg_number;
    @Column
    public String client_status;
    @Column
    public LocalDateTime client_timestamp;
    @Column
    public String client_logo;
    @Column
    public LocalDateTime client_last_updated;
    @Column
    public int client_contact_person_id;
    @Column
    public String client_fax;
    public LocalTime client_hour_opening = LocalTime.parse("08:00");
    public LocalTime client_hour_closing = LocalTime.parse("16:00");
    @Column
    public String client_bank_account_name;
    @Column
    public String client_bank_account_number;
    @Column
    public String client_bank_name;
    @Column
    public String client_bank_branch_code;
    @Column(columnDefinition = "varchar(255) default 'Business'", nullable = false)
    public String client_bank_account_Type = "Business";

    @Column(columnDefinition = "varchar(255) default 'Yes'", nullable = false)
    public String client_discount = "Yes";
    @Column(columnDefinition = "double default 50", nullable = false)
    public double client_discount_percentage = 50;
    @Column(columnDefinition = "int default 32", nullable = false)
    public double client_discount_timeframe = 32;

    @Column(columnDefinition = "varchar(255) default 'Yes'", nullable = false)
    public String client_courtesy = "Yes";
    @Column(columnDefinition = "double default 60", nullable = false)
    public double client_courtesy_amount = 60;
    @Column(columnDefinition = "int default 32", nullable = false)
    public double client_courtesy_timeframe = 32;

    @Column(columnDefinition = "varchar(255) default 'Yes'", nullable = false)
    public String client_enforcement = "Yes";
    @Column(columnDefinition = "double default 60", nullable = false)
    public double client_enforcement_amount = 60;
    @Column(columnDefinition = "int default 64", nullable = false)
    public double client_enforcement_timeframe = 64;

    @Column(columnDefinition = "varchar(255) default 'Yes'", nullable = false)
    public String client_warrant = "Yes";
    @Column(columnDefinition = "double default 150", nullable = false)
    public double client_warrant_amount = 150;
    @Column(columnDefinition = "int default 96", nullable = false)
    public double client_warrant_timeframe = 96;


}
