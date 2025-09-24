package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.invoice;
import com.mncedicy.stims.api.Model.invoice_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceItemRepo extends JpaRepository<invoice_item,Long> {

    @Query("SELECT a FROM invoice_item a WHERE a.invoice_item_client_id = :invoice_item_client_id")
    List<invoice_item> findByClientId(@Param("invoice_item_client_id") int invoice_item_client_id);

    @Query("SELECT a FROM invoice_item a WHERE a.invoice_item_invoice_number = :invoice_item_invoice_number")
    List<invoice_item> findByInvoiceNumber(@Param("invoice_item_invoice_number") long invoice_item_invoice_number);
}
