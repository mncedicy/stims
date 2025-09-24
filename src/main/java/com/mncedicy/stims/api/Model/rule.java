package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("ALL")
@Entity
public class rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long rule_id;
    public String rule_name;
    public String rule_description;
    public String rule_trigger; //   email/sms/status
    public String rule_when;
    public int rule_days_repeats;
    public String rule_type; //   send/update
    public LocalDate rule_date_from;
    public LocalDate rule_date_to;
    public LocalTime rule_time;
    public String rule_doc_type;
    public String rule_grouping;
    @Column(length = 5000)
    public String rule_message;
    @Column(columnDefinition = "varchar(255) default 'Active'", nullable = false)
    public String rule_status = "Active";
    public LocalDateTime rule_last_update;
    public long rule_updated_by;
    public String rule_updated_by_name;
    public LocalDateTime rule_timestamp =LocalDateTime.now();
    public long rule_created_by;
    public int rule_client_id;
    public String rule_created_by_name;
    public int rule_triggered_count;
    public String rule_range;
    public LocalDate rule_start_date;
}
