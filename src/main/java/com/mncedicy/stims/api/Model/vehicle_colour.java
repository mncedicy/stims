package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class vehicle_colour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int vehicle_colour_id;
    public String vehicle_colour_description;
}