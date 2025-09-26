package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.ikhokha_payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IkhokhaPaymentRepo extends JpaRepository<ikhokha_payment,Long> {

    @Query("SELECT a FROM ikhokha_payment a WHERE a.ikhokha_payment_client_id = :client_id")
    List<ikhokha_payment> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM ikhokha_payment a WHERE a.ikhokha_payment_paylink_id = :paylinkId")
    List<ikhokha_payment> findByPayLinkId(@Param("paylinkId") String paylinkId);

    @Query("SELECT a FROM ikhokha_payment a WHERE a.ikhokha_payment_notice_reference = :notice_reference")
    List<ikhokha_payment> findByNoticeReference(@Param("issued_to") String notice_reference);


}
