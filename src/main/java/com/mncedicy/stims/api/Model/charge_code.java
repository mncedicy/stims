package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
public class charge_code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long charge_code_id;
    public String charge_code_zone_type;
    public int charge_code;
    public String charge_code_str;
    public String charge_code_type;
    @Column(length = 5000)
    public String charge_code_regulation;
    public String charge_code_short_description;
    public String charge_code_fine_amount;
    public String charge_code_vehicle_type;
    public int charge_code_zone;
    public int charge_code_min_speed;
    public int charge_code_max_speed;
    public String charge_code_category;
    public int charge_code_client_id;
    @Column(length = 5000)
    public String charge_code_description;
    public LocalDateTime charge_code_timestamp;
    public LocalDateTime charge_code_last_update;
    public int charge_code_saved_by;
    public int charge_code_updated_by;
    public int charge_code_deleted_by;
    public LocalDateTime charge_code_delete_timestamp;
    public String charge_code_status;
}

