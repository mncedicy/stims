package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long role_privilege_id;
    public int role_privilege_role_id;
    public long role_privilege_privilege_id;
    public int role_privilege_client_id;
    @Column(columnDefinition = "varchar(255) default 'Active'", nullable = false)
    public String role_privilege_status ="Active";
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime role_privilege_timestamp= LocalDateTime.now();
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime role_privilege_last_update= LocalDateTime.now();
    public String role_privilege_path;
}
