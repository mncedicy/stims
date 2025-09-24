package com.mncedicy.stims.api.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@SuppressWarnings("ALL")
@Entity
public class book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long book_id;
    public int book_authority_code;
    public String book_authority_name;
    public int book_pages;
    public int book_first_number;
    public int book_last_number;
    public int book_type_code;
    public String book_status;
    public String book_type_name;
    public int book_pages_left;
    public int book_pages_completed;
    public LocalDateTime book_timestamp;
    public LocalDateTime book_last_update;
    public long book_updated_by;
    public long book_issued_to;
    public String book_issued_to_name;
    public LocalDateTime book_issue_date;
    public long book_issued_by;
    public long book_handedin_by;
    public LocalDateTime book_handedin_date;
    public int book_client_id;
    public long book_book_batch_id;
    public String book_first_number_complete;
    public String book_last_number_complete;
    public String book_issued_by_name;
    public String book_handedin_by_name;
    public long book_captured_by;
    public String book_captured_by_name;
    public LocalDateTime book_capture_date;
    public LocalDateTime book_completed_date;
    public int book_first_number_check;
    public int book_last_number_check;
    public LocalDateTime book_issued_timestamp;
    public LocalDateTime book_handedin_timestamp;
    @Column(length = 5000)
    public String book_notice_list;
    public void increment_pages_completed() {
        this.book_pages_completed++;
    }
}
