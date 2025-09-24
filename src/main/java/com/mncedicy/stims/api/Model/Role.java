package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int role_id;
    public String role_name;
    public String role_description;
    public int role_client_id;
    @Column(columnDefinition = "varchar(255) default '/Home'", nullable = false)
    public String role_landing_page ="/Home";
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime role_timestamp= LocalDateTime.now();
    @Column(columnDefinition = "varchar(255) default 'Active'", nullable = false)
    public String role_status = "Active";
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime role_last_update = LocalDateTime.now();
    @Column(columnDefinition = "varchar(255) default 'Yes'", nullable = false)
    public String role_deletable = "Yes";
    @Column(columnDefinition = "varchar(255) default 'No'", nullable = false)
    public String role_mobile_app = "No";
    @Column(columnDefinition = "varchar(255) default 'General'", nullable = false)
    public String role_category = "General";
    @Column(columnDefinition = "int default 0", nullable = false)
    public int role_target = 0;
}
