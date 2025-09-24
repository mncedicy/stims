package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class enatis_type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int enatis_type_id;
    public String enatis_type_code;
    public int enatis_type_language_id;
    public String enatis_type_description;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime enatis_type_last_update= LocalDateTime.now();

}
