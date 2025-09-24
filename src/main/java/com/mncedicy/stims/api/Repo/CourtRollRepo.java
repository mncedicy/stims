package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.court_roll;
import com.mncedicy.stims.api.Model.invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourtRollRepo extends JpaRepository<court_roll,Long> {

    @Query("SELECT a FROM court_roll a WHERE a.court_roll_client_id = :court_roll_client_id order by a.court_roll_id desc")
    List<court_roll> findByClientId(@Param("court_roll_client_id") int court_roll_client_id);

    @Query("SELECT a FROM court_roll a WHERE a.court_roll_court_id = :court_roll_court_id")
    List<court_roll> findByCourtId(@Param("court_roll_court_id") int court_roll_court_id);


}
