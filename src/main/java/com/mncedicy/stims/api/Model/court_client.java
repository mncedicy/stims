package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class court_client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int court_client_id;
    public int court_client_court_id;
    public int court_client_client_id;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime court_client_timestamp;
    public int court_client_created_by;
    @Column(columnDefinition = "varchar(255) default 'active'")
    public String court_client_status;
    public LocalDateTime court_client_delete_timestamp;
    public int court_client_deleted_by;
    @Column(columnDefinition = "varchar(255) default 500")
    public int court_client_roll_max_records;
}
