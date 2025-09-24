package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Contact;
import com.mncedicy.stims.api.Model.book_batch;
import com.mncedicy.stims.api.Model.charge_code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargeCodeRepo extends JpaRepository<charge_code,Long> {

    @Query("SELECT a FROM charge_code a WHERE a.charge_code_client_id = :client_id")
    List<charge_code> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM charge_code a WHERE a.charge_code_client_id = :client_id and " +
            "a.charge_code_str like %:search_keyword% or a.charge_code_description like %:search_keyword% " +
            "or a.charge_code_short_description like %:search_keyword%")
    List<charge_code> findByClientIdSearch(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);

    @Query("SELECT a FROM charge_code a WHERE a.charge_code = :code")
    List<charge_code> findByChargeCode(@Param("code") int code);

}
