package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class vehicle_usage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int vehicle_usage_id;
    public String vehicle_usage_code;
    public int vehicle_usage_language_id;
    public String vehicle_usage_description;
    public LocalDateTime vehicle_usage_last_update;
}


