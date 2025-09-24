package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.infringement_infringer;
import com.mncedicy.stims.api.Model.infringement_notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfringerRepo extends JpaRepository<infringement_infringer,Long> {

    @Query("SELECT a FROM infringement_infringer a WHERE a.infringement_infringer_client_id = :infringement_infringer_client_id")
    List<infringement_infringer> findByClientId(@Param("infringement_infringer_client_id") int infringement_infringer_client_id);

    @Query("SELECT a FROM infringement_infringer a WHERE a.infringement_infringer_notice_id = :infringement_infringer_notice_id")
    List<infringement_infringer> findByNoticeId(@Param("infringement_infringer_notice_id") long infringement_infringer_notice_id);

    @Query("SELECT a FROM infringement_infringer a WHERE a.infringement_infringer_notice_id = :infringement_infringer_notice_id" +
            " and a.infringement_infringer_status = :infringement_infringer_status")
    List<infringement_infringer> findByNoticeIdAndStatus(@Param("infringement_infringer_notice_id") long infringement_infringer_notice_id,
                                                @Param("infringement_infringer_status") String infringement_infringer_status);


    @Query("SELECT a FROM infringement_infringer a WHERE a.infringement_infringer_status = :infringement_infringer_status")
    List<infringement_infringer> findByStatus(@Param("infringement_infringer_status") String infringement_infringer_status);

    @Query("SELECT a FROM infringement_infringer a WHERE a.infringement_infringer_notice_reference = :infringement_infringer_notice_reference")
    List<infringement_infringer> findByNoticeRef(@Param("infringement_infringer_notice_reference") String infringement_infringer_notice_reference);

}
