package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.rule;
import com.mncedicy.stims.api.Model.rule_receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RuleReceiverRepo extends JpaRepository<rule_receiver,Long> {

 @Query("SELECT a FROM rule_receiver a WHERE a.rule_receiver_client_id = :client_id")
    List<rule_receiver> findByClientId(@Param("client_id") int client_id);


    @Query("SELECT a FROM rule_receiver a WHERE a.rule_receiver_rule_id = :rule_id")
    List<rule_receiver> findByRuleId(@Param("rule_id") long rule_id);

}
