package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.book_batch;
import com.mncedicy.stims.api.Model.enatis_file;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnatisFileRepo extends JpaRepository<enatis_file,Long> {

    @Query("SELECT a FROM enatis_file a WHERE a.enatis_file_client_id = :enatis_file_client_id")
    List<enatis_file> findByClientId(@Param("enatis_file_client_id") int enatis_file_client_id);

    @Query("SELECT a FROM enatis_file a WHERE a.enatis_file_client_id = :enatis_file_client_id " +
            "order by enatis_file_id desc limit 1")
    List<enatis_file> findLastByClientId(@Param("enatis_file_client_id") int enatis_file_client_id);
}
