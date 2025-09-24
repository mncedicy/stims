package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class enatis_make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int enatis_make_id;
    public String enatis_make_code;
    public int enatis_make_language_id;
    public String enatis_make_description;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime enatis_make_last_update= LocalDateTime.now();

}
