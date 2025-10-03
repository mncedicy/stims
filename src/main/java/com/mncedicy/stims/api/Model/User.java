package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int user_id;
    public String user_username;

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    @Column(length = 60)
    private String user_password;
    public String user_status;
    public int user_client_id;
    public long user_person_id;
    public LocalDateTime user_last_update= LocalDateTime.now();
    @CurrentTimestamp
    public LocalDateTime user_timestamp= LocalDateTime.now();
    public int user_login_count_today;
    public int user_login_count_total;
    public LocalDateTime user_last_login;
    public int user_attempt_count;
    public String user_type;
    public int user_role_id;
    public String user_role_name;
    public String user_location;
    public String user_client_type;
    public String user_image;
    public String user_officer_victor_number;
    public String user_officer_inspector_number;
    public String user_officer_type;
    @Column(columnDefinition = "varchar(255) default 'Temporary'", nullable = false)
    public String user_password_type = "Temporary";
    public String user_login_type;
    public String user_app_token;
    public String user_web_token;
    public String user_person_name;
}
