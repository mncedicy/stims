package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.infringement_notice;
import com.mncedicy.stims.api.Model.notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepo extends JpaRepository<notification,Long> {

    @Query("SELECT a FROM notification a WHERE a.notification_client_id = :client_id")
    List<notification> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM notification a WHERE a.notification_notice_id = :notice_id")
    List<notification> findByNoticeId(@Param("notice_id") long notice_id);
    @Query("SELECT a FROM notification a WHERE a.notification_sender_id = :sender_id")
    List<notification> findBySenderId(@Param("sender_id") long sender_id);


    @Query("SELECT a FROM notification a WHERE a.notification_receiver_id = :receiver_id")
    List<notification> findByReceiverId(@Param("receiver_id") long receiver_id);


    @Query("SELECT a FROM notification a WHERE a.notification_receiver_id = :send_id " +
            "or a.notification_sender_id = :send_id order by notification_last_update desc")
    List<notification> findBySenderOrReceiverId(@Param("send_id") long send_id);

    @Query("SELECT a FROM notification a WHERE a.notification_status = :status")
    List<notification> findByStatus(@Param("status") String status);

    @Query("SELECT a FROM notification a WHERE a.notification_receiver_id = :receiver_id " +
            "and a.notification_status='Unread'")
    List<notification> findByReceiverUnread(@Param("receiver_id") long receiver_id);

    @Query("SELECT a FROM notification a WHERE a.notification_sender_id = :sender_id " +
            "and a.notification_status='Unread'")
    List<notification> findBySenderUnread(@Param("sender_id") long sender_id);


    @Query("SELECT count(a) FROM notification a where " +
            "a.notification_receiver_id=:receiver_id or " +
            "a.notification_sender_id=:receiver_id")
    Integer CountAllByReceiver(@Param("receiver_id") long receiver_id);



}


