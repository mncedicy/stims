package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Classes.CountData;
import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.book_batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<book,Long> {

    @Query("SELECT a FROM book a WHERE a.book_client_id = :client_id")
    List<book> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM book a WHERE a.book_book_batch_id = :batch_id")
    List<book> findByBookBatchId(@Param("batch_id") int batch_id);

    @Query("SELECT a FROM book a WHERE a.book_issued_to = :issued_to")
    List<book> findByBookOfficerId(@Param("issued_to") long issued_to);


    @Query("SELECT count(a) FROM book a where a.book_issued_to=:issued_to")
    Integer CountAllByfficerId(@Param("issued_to") long issued_to);



    @Query("SELECT count(a) FROM book a where a.book_issued_to=:issued_to and a.book_status='handedin'")
    Integer CountHandedinByfficerId(@Param("issued_to") long issued_to);


    @Query("SELECT count(a) FROM book a where a.book_status !='new'")
    Integer CountAllIssued();
    @Query("SELECT count(a) FROM book a where a.book_status='handedin'")
    Integer CountAllHandedin();


}
