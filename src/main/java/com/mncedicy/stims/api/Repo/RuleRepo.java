package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RuleRepo extends JpaRepository<rule,Long> {

 @Query("SELECT a FROM rule a WHERE a.rule_client_id = :client_id")
    List<rule> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM rule a WHERE a.rule_name = :rule_name")
    List<rule> findByName(@Param("rule_name") String rule_name);


    @Query("SELECT a FROM rule a WHERE a.rule_status = 'Active'")
    List<rule> findAllActive();

}
