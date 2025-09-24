package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.court_roll;
import com.mncedicy.stims.api.Model.court_roll_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourtRollItemRepo extends JpaRepository<court_roll_item,Long> {

    @Query("SELECT a FROM court_roll_item a WHERE a.court_roll_item_client_id = :client_id")
    List<court_roll_item> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM court_roll_item a WHERE a.court_roll_item_court_roll_id = :item_court_roll_id")
    List<court_roll_item> findByCourtRollId(@Param("item_court_roll_id") long item_court_roll_id);

    @Query("SELECT a FROM court_roll_item a WHERE a.court_roll_item_court_roll_id = :item_court_roll_id " +
            "and a.court_roll_item_status !='Printed'")
    List<court_roll_item> findByCourtRollIdCaptured(@Param("item_court_roll_id") long item_court_roll_id);

}
