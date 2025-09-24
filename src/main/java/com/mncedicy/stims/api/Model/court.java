package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int court_id;
    public String court_region;
    public String court_magisterial_district;
    public String court_status;
    public String court_type;
    public String court_office;
    public String equality_court;
    public String small_claims_court;
    public String court_telephone;
    public String court_fax;
    public String court_postal_address;
    public String court_physical_address;
    public String court_gps;
    public LocalDateTime court_timestamp;
    public LocalDateTime court_last_update;
    public int court_updated_by;
}

