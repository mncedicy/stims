package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Client;
import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.book_batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookBatchRepo extends JpaRepository<book_batch,Integer> {

    @Query("SELECT a FROM book_batch a WHERE a.book_batch_client_id = :book_batch_client_id")
    List<book_batch> findByClientId(@Param("book_batch_client_id") int book_batch_client_id);

}
