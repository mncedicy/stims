package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.book_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookItemRepo extends JpaRepository<book_item,Integer> {

    @Query("SELECT a FROM book_item a WHERE a.book_item_client_id = :book_item_client_id")
    List<book_item> findByClientId(@Param("book_item_client_id") int book_item_client_id);

    @Query("SELECT a FROM book_item a WHERE a.book_item_book_batch_id = :book_item_book_batch_id")
    List<book_item> findByBookBatchId(@Param("book_item_book_batch_id") int book_item_book_batch_id);

    @Query("SELECT a FROM book_item a WHERE a.book_item_book_id = :book_item_book_id order by book_item_captured_by desc")
    List<book_item> findByBookId(@Param("book_item_book_id") long book_item_book_id);

    @Query("SELECT a FROM book_item a WHERE a.book_item_book_id = :book_id order by a.book_item_id asc")
    List<book_item> findByBookIdAsc(@Param("book_id") long book_id);
    @Query("SELECT a FROM book_item a WHERE a.book_item_notice_number_complete = :number_complete")
    List<book_item> findByNoticeComplete(@Param("number_complete") String number_complete);


    @Query("SELECT count(a) FROM book_item a where a.book_item_book_issued_to=:book_issued_to")
    Integer CountAllByfficerId(@Param("book_issued_to") long book_issued_to);

    @Query("SELECT count(a) FROM book_item a where a.book_item_book_issued_to=:book_issued_to and a.book_item_status='Captured'")
    Integer CountCapturedByfficerId(@Param("book_issued_to") long book_issued_to);


    @Query("SELECT count(a) FROM book_item a where a.book_item_status !='New'")
    Integer CountAllIssued();

    @Query("SELECT count(a) FROM book_item a where a.book_item_status='Captured'")
    Integer CountAllCaptured();




}
