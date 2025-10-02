package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long invoice_number;
    public String invoice_name_to;
    public String invoice_name_from;
    public long invoice_created_by;
    public String invoice_created_by_name;
    public String invoice_payment_type;
    public String invoice_payment_subtype;
    public LocalDate invoice_payment_date;
    public LocalDateTime invoice_timestamp= LocalDateTime.now();
    public String invoice_description;
    public int invoice_client_id;
    public double invoice_amount;
    public int invoice_records;
    public String invoice_uuid;

    @PrePersist
    public void generateUserUuid() {
        if (invoice_uuid == null) {
            invoice_uuid = String.valueOf(UUID.randomUUID());
        }
    }

}



