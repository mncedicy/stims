package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.infringement_notice;
import com.mncedicy.stims.api.Model.invoice;
import com.mncedicy.stims.api.Model.invoice_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepo extends JpaRepository<invoice,Long> {

    @Query("SELECT a FROM invoice a WHERE a.invoice_client_id = :invoice_client_id")
    List<invoice> findByClientId(@Param("invoice_client_id") int invoice_client_id);

    @Query("SELECT a FROM invoice a WHERE a.invoice_number = :invoice_number")
    List<invoice> findByInvoiceNumber(@Param("invoice_number") long invoice_number);


    @Query("SELECT a FROM invoice a WHERE " +
            "a.invoice_payment_date between :date_from and :date_to " +
            "and a.invoice_client_id = :client_id " +
            "order by a.invoice_payment_date asc")
    List<invoice> findInvoiceDateRange(@Param("client_id") int client_id,
                                                        @Param("date_from") LocalDate date_from,
                                                        @Param("date_to") LocalDate date_to);



}
