package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@SuppressWarnings("ALL")
@Entity
public class book_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long book_item_id;
    public long book_item_book_id;
    public long book_item_book_batch_id;
    public int book_item_client_id;
    public String book_item_notice_number_complete;
    public int book_item_notice_type;
    public int book_item_notice_number;
    public int book_item_notice_check;
    public int book_item_notice_post;
    public String book_item_status;
    public String book_item_description;
    public LocalDateTime book_item_timestamp;
    public LocalDateTime book_item_last_update;
    public long book_item_updated_by;
    public LocalDateTime book_item_status_date;
    public String book_item_status_reason;
    public long book_item_status_by;
    public String book_item_captured_by_name;
    public long book_item_captured_by;
    public LocalDateTime book_item_captured_date;

    public String book_item_book_issued_by_name;
    public long book_item_book_issued_by;
    public long book_item_book_issued_to;
    public String book_item_book_issued_to_name;
    public LocalDateTime book_item_book_issue_date;

    public void setBook_item_status(String book_item_status) {
        this.book_item_status = book_item_status;
    }
}

