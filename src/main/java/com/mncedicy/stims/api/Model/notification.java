package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long notification_id;
    @Column(columnDefinition = "varchar(255) default 'New'", nullable = false)
    public String notification_status = "New";
    public String notification_type;
    public LocalDateTime notification_read_date;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime notification_last_update= LocalDateTime.now();
    public long notification_updated_by;
    public long notification_receiver_id;
    public String notification_receiver_name;
    public String notification_sender_name;
    public long notification_sender_id;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime notification_sent_date= LocalDateTime.now();
    public int notification_client_id;
    public long notification_notice_id;
    public String notification_reference;
    @Column(length = 5000)
    public String notification_message;
    public String notification_subject;
    public String notification_description;
    public String notification_category;
    public String notification_attachment;

}
