package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.history;
import com.mncedicy.stims.api.Model.infringement_notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepo extends JpaRepository<history,Long> {

    @Query("SELECT a FROM history a WHERE a.history_client_id = :history_client_id")
    List<history> findByClientId(@Param("history_client_id") int history_client_id);

    @Query("SELECT a FROM history a WHERE a.history_reference_id = :history_reference_id and " +
            "a.history_reference_type = :history_reference_type")
    List<history> findByReference(@Param("history_reference_id") long client_id,@Param("history_reference_type") String history_reference_type);



}


