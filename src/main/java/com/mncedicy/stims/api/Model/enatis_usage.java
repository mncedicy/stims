package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class enatis_usage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int enatis_usage_id;
    public String enatis_usage_code;
    public int enatis_usage_language_id;
    public String enatis_usage_description;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime enatis_usage_last_update= LocalDateTime.now();

}
