package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class vehicle_model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int vehicle_model_id;
    public String vehicle_model_description;
    public int vehicle_model_make_id;
    public String vehicle_model_make;

}
