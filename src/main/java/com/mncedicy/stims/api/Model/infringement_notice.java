package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class infringement_notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long infringement_notice_id;
    public String infringement_notice_reference;
    public String infringement_notice_id_number;

    public String infringement_notice_registration;
    public LocalDateTime infringement_notice_last_update;
    public LocalDateTime infringement_notice_timestamp;
    public int infringement_notice_client_id;
    public int infringement_notice_charge_code;
    @Column(length = 2000)
    public String infringement_notice_charge_description;
    public String infringement_notice_charge_short_description;
    public String infringement_notice_name;
    public String infringement_notice_status;
    public String infringement_notice_access_status="Open";
    @Column(columnDefinition = "varchar(255) default 'Notice'", nullable = false)
    public String infringement_notice_letter_status = "Notice";
    public LocalDate infringement_notice_letter_status_date = LocalDate.now();
    public LocalDateTime infringement_notice_status_date;
    public long infringement_notice_status_updated_by;
    public String infringement_notice_status_updated_by_name;
    public String infringement_notice_last_status;
    public String infringement_notice_payment_status;
    public LocalDate infringement_notice_court_date;
    public long infringement_notice_payment_receipt_number;
    public double infringement_notice_payment_amount;
    public String infringement_notice_reduce_status;
    public String infringement_notice_withdraw_status;
    public String infringement_notice_nomination_status;
    public int infringement_notice_type;
    public String infringement_notice_type_name;
    public int infringement_notice_year;
    public int infringement_notice_month;
    public String infringement_notice_rejection_status;
    public String infringement_notice_postpone_status;
    public String infringement_notice_summon_status;
    @Column(columnDefinition = "varchar(255) default 'no'", nullable = false)
    public String infringement_notice_court_roll_printed="no";
    public String infringement_notice_warrant_status;
    public double infringement_notice_warrant_amount;
    public String infringement_notice_fine_amount;
    public double infringement_notice_final_amount;
    public long infringement_notice_captured_by;
    public String infringement_notice_captured_by_name;
    public String infringement_notice_officer_name;
    public long infringement_notice_officer_id;
    public int infringement_notice_authority_code;
    public int infringement_notice_reference_sequence;
    public LocalDate infringement_notice_offence_date;
    public LocalDateTime infringement_notice_offence_time;
    public String infringement_notice_offence_location;
    public String infringement_notice_offence_location_city;
    public String infringement_notice_offence_location_code;
    public int infringement_notice_court_id;
    public String infringement_notice_court_name;


    public String infringement_notice_vehicle_make;
    public String infringement_notice_vehicle_model;
    public String infringement_notice_vehicle_colour;
    public String infringement_notice_vehicle_usage;
    public String infringement_notice_vehicle_type;
    public LocalDate infringement_notice_vehicle_license_expire;
    public LocalDate infringement_notice_vehicle_ownership_date;
    public LocalDate infringement_notice_vehicle_first_reg_date;
    public LocalDate infringement_notice_payment_due_date;
    public long infringement_notice_holder_id;
    public String infringement_notice_holder_value;
    public String infringement_notice_holder_value1;
    public long infringement_notice_invoice_number;
    public long infringement_notice_book_id;
    public double infringement_notice_holder_value_double;
    public String infringement_notice_holder_value2;
    public String infringement_notice_holder_value3;
    public String infringement_notice_holder_value4;
    public String infringement_notice_saved_note;
    public long infringement_notice_saved_by;
    public String infringement_notice_saved_by_name;
    public long infringement_notice_submitted_by;
    public String infringement_notice_submitted_by_name;
    public String infringement_notice_enatis;
    public String infringement_notice_enatis_status;
    public long infringement_notice_enatis_file;
    public int infringement_notice_enatis_export_count;
    public LocalDateTime infringement_notice_enatis_export_date;
    public String infringement_notice_enatis_exported_by_name;
    public long infringement_notice_enatis_exported_by;
    public String infringement_notice_enatis_verified_by_name;
    public long infringement_notice_enatis_verified_by;
    public LocalDateTime infringement_notice_enatis_verify_date;
    public LocalDate infringement_notice_capture_date;
    public LocalDateTime infringement_notice_capture_timestamp;
    public LocalDate infringement_notice_enatis_capture_date;
    public LocalDateTime infringement_notice_enatis_capture_timestamp;
    public String infringement_notice_email;
    public String infringement_notice_cellphone;
    public String infringement_notice_client_name;
    public String infringement_notice_uuid;


    @PrePersist
    public void generateUserUuid() {
        if (infringement_notice_uuid == null) {
            infringement_notice_uuid = String.valueOf(UUID.randomUUID());
        }
    }

    public double getInfringement_notice_final_amount() {
        return infringement_notice_final_amount;
    }
}


