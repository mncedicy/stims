package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.infringement_notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepo extends JpaRepository<infringement_notice,Long> {

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id")
    List<infringement_notice> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_captured_by = :captured_by " +
            "and (a.infringement_notice_status='Notice' or a.infringement_notice_status='Saved') " +
            "order by a.infringement_notice_id desc, a.infringement_notice_status desc")
    List<infringement_notice> findByCapturedById(@Param("captured_by") long captured_by);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_book_id = :book_id " +
            "order by a.infringement_notice_id desc, a.infringement_notice_status asc")
    List<infringement_notice> findByBookId(@Param("book_id") long book_id);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_officer_id = :officer_id")
    List<infringement_notice> findByOfficerId(@Param("officer_id") long officer_id);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_status = :status")
    List<infringement_notice> findByStatus(@Param("status") String status);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_access_status = :access_status")
    List<infringement_notice> findByAccessStatus(@Param("access_status") String access_status);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_enatis = 'Active' and " +
            "a.infringement_notice_status != 'Saved' " +
            "order by a.infringement_notice_id asc")
    List<infringement_notice> findByClientEnatisActive(@Param("client_id") int client_id);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_enatis = 'Active' and " +
            "a.infringement_notice_status != 'Saved' and " +
            "(a.infringement_notice_enatis_status = 'Ready' or a.infringement_notice_enatis_status = 'Not Found') " +
            "order by a.infringement_notice_id asc")
    List<infringement_notice> findByClientEnatisActiveExport(@Param("client_id") int client_id);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_reference = :search_keyword and " +
            "a.infringement_notice_status != 'Saved' order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientReference(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_reference = :search_keyword and a.infringement_notice_enatis = 'Active' and " +
            "a.infringement_notice_status != 'Saved' order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientReferenceEnatisActive(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_id_number = :search_keyword and " +
            "a.infringement_notice_status != 'Saved' order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientIdNumber(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_registration = :search_keyword " +
            "and a.infringement_notice_status != 'Saved' " +
            "order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientRegistration(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);



    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_access_status LIKE %:access_status% and a.infringement_notice_status != 'Saved' and " +
            "a.infringement_notice_reference = :search_keyword order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientReferenceAccessStatus(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword,@Param("access_status") String access_status);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_access_status LIKE %:access_status% and a.infringement_notice_status != 'Saved' and " +
            "a.infringement_notice_id_number = :search_keyword order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientIdNumberAccessStatus(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword,@Param("access_status") String access_status);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_access_status LIKE %:access_status% and a.infringement_notice_status != 'Saved' and " +
            "a.infringement_notice_registration = :search_keyword order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientRegistrationAccessStatus(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword,@Param("access_status") String access_status);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_id_number = :id_number and " +
            "a.infringement_notice_status!='Saved' and a.infringement_notice_access_status='Open' " +
            "order by a.infringement_notice_status desc, a.infringement_notice_offence_date asc")
    List<infringement_notice> findByClientIdNumberOpen(@Param("client_id") int client_id,@Param("id_number") String id_number);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "a.infringement_notice_status!='Saved' and " +
            "(a.infringement_notice_reference = :search_keyword or " +
            "a.infringement_notice_id_number = :search_keyword or " +
            "a.infringement_notice_registration = :search_keyword) order by a.infringement_notice_access_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientAll(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_client_id = :client_id and " +
            "(a.infringement_notice_reference = :search_keyword or " +
            "a.infringement_notice_id_number = :search_keyword or " +
            "a.infringement_notice_registration = :search_keyword) and " +
            "a.infringement_notice_status!='Saved' and " +
            "infringement_notice_access_status = 'Open' order by a.infringement_notice_status desc, a.infringement_notice_final_amount desc")
    List<infringement_notice> findByClientAllOpen(@Param("client_id") int client_id,@Param("search_keyword") String search_keyword);


    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_court_date = :court_date " +
            "and a.infringement_notice_access_status='Open' and " +
            "a.infringement_notice_status!='Saved' and a.infringement_notice_court_roll_printed='no'")
    List<infringement_notice> findByCourtDate(@Param("court_date") LocalDate court_date);

    @Query("SELECT a FROM infringement_notice a WHERE a.infringement_notice_court_date <= curdate() " +
            "and a.infringement_notice_court_id = :court_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved' " +
            "and a.infringement_notice_type_name=:notice_type " +
            "and a.infringement_notice_court_roll_printed='no'")
    List<infringement_notice> findTodayCourtRoll(@Param("court_id") int court_id,@Param("notice_type") String notice_type);

    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_letter_status_date between :date_from and :date_to " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved' " +
            "and a.infringement_notice_letter_status= :search_with")
    List<infringement_notice> findLetterByDateRange(@Param("client_id") int client_id,
                                                    @Param("search_with") String search_with,
                                                    @Param("date_from") LocalDate date_from,
                                                    @Param("date_to") LocalDate date_to);

    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_offence_date between :date_from and :date_to " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_status!='Saved' " +
            "and a.infringement_notice_type_name LIKE %:type_name% " +
            "and a.infringement_notice_access_status LIKE %:access_status% " +
            "order by a.infringement_notice_offence_date asc")
    List<infringement_notice> findNoticeTypeByDateRange(@Param("client_id") int client_id,
                                                    @Param("type_name") String type_name,
                                                    @Param("date_from") LocalDate date_from,
                                                    @Param("date_to") LocalDate date_to,
                                                    @Param("access_status") String access_status);

    @Query("SELECT count(a) FROM infringement_notice a where " +
            "a.infringement_notice_officer_id=:officer_id and a.infringement_notice_status != 'Saved'")
    Integer CountAllByfficerId(@Param("officer_id") long officer_id);

    @Query("SELECT count(a) FROM infringement_notice a where " +
            "a.infringement_notice_officer_id=:officer_id and " +
            "a.infringement_notice_access_status='Closed'")
    Integer CountClosedByfficerId(@Param("officer_id") long officer_id);



    @Query("SELECT count(a) FROM infringement_notice a where a.infringement_notice_status != 'Saved'")
    Integer CountAllNotice();

    @Query("SELECT count(a) FROM infringement_notice a where " +
            "a.infringement_notice_access_status='Closed'")
    Integer CountAllNoticeClosed();



    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_capture_date = :capture_date " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved'")
    List<infringement_notice> findByCaptureDate(@Param("client_id") int client_id,
                                                    @Param("capture_date") LocalDate capture_date);

    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_offence_date = :offence_date " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved'")
    List<infringement_notice> findByOffenceDate(@Param("client_id") int client_id,
                                                @Param("offence_date") LocalDate offence_date);

    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_court_date = :court_date " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved'")
    List<infringement_notice> findByCourtDate(@Param("client_id") int client_id,
                                                @Param("court_date") LocalDate court_date);

    @Query("SELECT a FROM infringement_notice a WHERE " +
            "a.infringement_notice_payment_due_date = :payment_date " +
            "and a.infringement_notice_client_id = :client_id " +
            "and a.infringement_notice_access_status='Open' " +
            "and a.infringement_notice_status!='Saved'")
    List<infringement_notice> findByPaymentDate(@Param("client_id") int client_id,
                                              @Param("payment_date") LocalDate payment_date);


}


