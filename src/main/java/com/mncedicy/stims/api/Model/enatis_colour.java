package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class enatis_colour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int enatis_colour_id;
    public String enatis_colour_code;
    public int enatis_colour_language_id;
    public String enatis_colour_description;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime enatis_colour_last_update= LocalDateTime.now();

}