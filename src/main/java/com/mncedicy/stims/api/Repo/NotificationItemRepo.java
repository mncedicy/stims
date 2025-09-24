package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.notification;
import com.mncedicy.stims.api.Model.notification_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationItemRepo extends JpaRepository<notification_item,Long> {

    @Query("SELECT a FROM notification_item a WHERE a.notification_item_client_id = :client_id")
    List<notification_item> findByClientId(@Param("client_id") int client_id);


    @Query("SELECT a FROM notification_item a WHERE a.notification_item_notification_id = :item_notification_id")
    List<notification_item> findByNotificationId(@Param("item_notification_id") long item_notification_id);

    @Query("SELECT a FROM notification_item a WHERE a.notification_item_notice_id = :item_notice_id")
    List<notification_item> findByNoticeId(@Param("item_notice_id") long item_notice_id);


    @Query("SELECT count(a) FROM notification_item a where " +
            "a.notification_item_receiver_id=:receiver_id and " +
            "a.notification_item_status='Unread'")
    Integer CountReceiverUnread(@Param("receiver_id") long receiver_id);


    @Query("SELECT a FROM notification_item a where " +
            "a.notification_item_receiver_id=:receiver_id and " +
            "a.notification_item_status='Unread'")
    List<notification_item> findReceiverUnread(@Param("receiver_id") long receiver_id);



}


