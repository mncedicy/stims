package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class vehicle_make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int vehicle_make_id;
    public String vehicle_make;

}
