package com.mncedicy.stims.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@SuppressWarnings("ALL")
@Entity
public class book_batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long book_batch_id;
    public int book_batch_authority_code;
    public String book_batch_authority_name;
    public int book_batch_pages_per_book;
    public int book_batch_books_count;
    public int book_batch_first_number;
    public int book_batch_last_number;
    public String book_batch_status;
    public int book_batch_type_code;
    public String book_batch_type_name;
    public LocalDateTime book_batch_timestamp;
    public LocalDateTime book_batch_last_update;
    public int book_batch_updated_by;
    public int book_batch_client_id;
    public String book_batch_first_number_complete;
    public String book_batch_last_number_complete;
    public int book_batch_first_number_check;
    public int book_batch_last_number_check;
    public int book_batch_captured_by;
    public String book_batch_captured_by_name;
    public LocalDateTime book_batch_capture_date;
    public int book_batch_print_count;

}
