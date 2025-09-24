package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class invoice_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long invoice_item_id;
    public long invoice_item_invoice_number;
    public String invoice_item_name_to;
    public String invoice_item_name_from;
    public long invoice_item_created_by;
    public String invoice_item_created_by_name;
    public String invoice_item_payment_type;
    public LocalDate invoice_item_payment_date;
    public LocalDateTime invoice_item_timestamp= LocalDateTime.now();
    public String invoice_item_description;
    public String invoice_item_reference;
    public String invoice_item_registration;
    public String invoice_item_id_number;
    public int invoice_item_client_id;
    public double invoice_item_amount;
}



