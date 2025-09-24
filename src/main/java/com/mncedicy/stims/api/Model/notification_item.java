package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class notification_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long notification_item_id;
    public long notification_item_notification_id;
    @Column(columnDefinition = "varchar(255) default 'Unread'", nullable = false)
    public String notification_item_status = "Unread";
    public String notification_item_type;
    public LocalDateTime notification_item_read_date;
    public LocalDateTime notification_item_last_update;
    public long notification_item_updated_by;
    public long notification_item_receiver_id;
    public String notification_item_receiver_name;
    public String notification_item_sender_name;
    public long notification_item_sender_id;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    public LocalDateTime notification_item_sent_date= LocalDateTime.now();
    public int notification_item_client_id;
    public long notification_item_notice_id;
    public String notification_item_reference;
    @Column(length = 5000)
    public String notification_item_message;
    public String notification_item_subject;
    public String notification_item_description;
    public String notification_item_category;
    public String notification_item_attachment;


    public notification_item(){}
    public notification_item(long notification_item_id, long notification_item_notification_id, String notification_item_status, String notification_item_type, LocalDateTime notification_item_read_date, LocalDateTime notification_item_last_update, long notification_item_updated_by, long notification_item_receiver_id, String notification_item_receiver_name, String notification_item_sender_name, long notification_item_sender_id, LocalDateTime notification_item_sent_date, int notification_item_client_id, long notification_item_notice_id, String notification_item_reference, String notification_item_message, String notification_item_subject, String notification_item_description, String notification_item_category, String notification_item_attachment) {
        this.notification_item_id = notification_item_id;
        this.notification_item_notification_id = notification_item_notification_id;
        this.notification_item_status = notification_item_status;
        this.notification_item_type = notification_item_type;
        this.notification_item_read_date = notification_item_read_date;
        this.notification_item_last_update = notification_item_last_update;
        this.notification_item_updated_by = notification_item_updated_by;
        this.notification_item_receiver_id = notification_item_receiver_id;
        this.notification_item_receiver_name = notification_item_receiver_name;
        this.notification_item_sender_name = notification_item_sender_name;
        this.notification_item_sender_id = notification_item_sender_id;
        this.notification_item_sent_date = notification_item_sent_date;
        this.notification_item_client_id = notification_item_client_id;
        this.notification_item_notice_id = notification_item_notice_id;
        this.notification_item_reference = notification_item_reference;
        this.notification_item_message = notification_item_message;
        this.notification_item_subject = notification_item_subject;
        this.notification_item_description = notification_item_description;
        this.notification_item_category = notification_item_category;
        this.notification_item_attachment = notification_item_attachment;
    }


    public notification_item clonee(){
        return new notification_item(this.notification_item_id, this.notification_item_notification_id, this.notification_item_status,
                this.notification_item_type, this.notification_item_read_date,
                this.notification_item_last_update, this.notification_item_updated_by,
                this.notification_item_receiver_id, this.notification_item_receiver_name,
                this.notification_item_sender_name, this.notification_item_sender_id,
                this.notification_item_sent_date, this.notification_item_client_id,
                this.notification_item_notice_id, this.notification_item_reference,
                this.notification_item_message, this.notification_item_subject,
                this.notification_item_description, this.notification_item_category,
                this.notification_item_attachment);

        }
}
