package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("ALL")
@Entity
public class rule_receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long rule_receiver_id;
    public long rule_receiver_rule_id;
    public String rule_receiver_name;
    public String rule_receiver_type; //   role/email
    public int rule_receiver_ref_id;
    public String rule_receiver_ref_name;
    public String rule_receiver_distination;
    @Column(columnDefinition = "varchar(255) default 'Active'", nullable = false)
    public String rule_receiver_status = "Active";
    public LocalDateTime rule_receiver_last_update;
    public long rule_receiver_updated_by;
    public String rule_receiver_updated_by_name;
    public LocalDateTime rule_receiver_timestamp =LocalDateTime.now();
    public long rule_receiver_created_by;
    public int rule_receiver_client_id;
    public String rule_receiver_created_by_name;
}
