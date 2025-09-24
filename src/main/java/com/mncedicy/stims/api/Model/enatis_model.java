package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class enatis_model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int enatis_model_id;
    public String enatis_model_code;
    public int enatis_model_language_id;
    public String enatis_model_description;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime enatis_model_last_update= LocalDateTime.now();

}