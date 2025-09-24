package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class infringement_infringer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long infringement_infringer_id;
    public long infringement_infringer_client_id;
    public long infringement_infringer_notice_id;
    public String infringement_infringer_notice_reference;
    public String infringement_infringer_status;
    public LocalDateTime infringement_infringer_timestamp;
    public LocalDateTime infringement_infringer_court_date;
    public LocalDateTime infringement_infringer_nomination_date;
    public String infringement_infringer_nomination_reason;
    public LocalDateTime infringement_infringer_sell_date;
    @Column(length = 5000)
    public String infringement_infringer_description;
    public long infringement_infringer_nomination_by;
    public long infringement_infringer_nomination_from_id;
    public String infringement_infringer_id_number;
    public String infringement_infringer_title;
    public String infringement_infringer_name;
    public String infringement_infringer_middle_name;
    public String infringement_infringer_surname;
    public String infringement_infringer_occupation;
    public String infringement_infringer_company;
    public String infringement_infringer_company_id;
    public String infringement_infringer_ownership_type;
    public String infringement_infringer_cellphone;
    public String infringement_infringer_email;
    public String infringement_infringer_physical_address;
    public String infringement_infringer_physical_suburb;
    public String infringement_infringer_physical_city;
    public String infringement_infringer_physical_code;
    public String infringement_infringer_postal_address;
    public String infringement_infringer_postal_suburb;
    public String infringement_infringer_postal_city;
    public String infringement_infringer_postal_code;


}
