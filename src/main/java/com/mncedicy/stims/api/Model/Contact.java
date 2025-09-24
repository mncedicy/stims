package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long contact_id;
    public String contact_cellphone;
    public String contact_telephone;
    public String contact_fax_number;
    public String contact_work_telephone;
    public String contact_home_telephone;
    public String contact_type;
    public String contact_email_address;
    public String contact_personal_email_address;
    public String contact_physical_address_street;
    public String contact_physical_address_suburb;
    public String contact_physical_address_city;
    public String contact_physical_address_province;
    public String contact_physical_address_code;
    public String contact_postal_address_street;
    public String contact_postal_address_suburb;
    public String contact_postal_address_province;
    public String contact_postal_address_city;
    public String contact_postal_address_code;
    @CurrentTimestamp
    public LocalDateTime contact_timestamp= LocalDateTime.now();
    public LocalDateTime contact_last_update = LocalDateTime.now();
    public String contact_latitude;
    public String contact_longitude;
    public String contact_website;


    
}
