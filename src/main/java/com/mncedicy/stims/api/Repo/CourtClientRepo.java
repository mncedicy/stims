package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.charge_code;
import com.mncedicy.stims.api.Model.court_client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourtClientRepo extends JpaRepository<court_client,Integer> {

    @Query("SELECT a FROM court_client a WHERE a.court_client_client_id = :client_id")
    List<court_client> findByClientId(@Param("client_id") int client_id);

}
