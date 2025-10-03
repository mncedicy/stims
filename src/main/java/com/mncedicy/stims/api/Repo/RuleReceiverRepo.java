package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.rule;
import com.mncedicy.stims.api.Model.rule_receiver;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RuleReceiverRepo extends JpaRepository<rule_receiver,Long> {

 @Query("SELECT a FROM rule_receiver a WHERE a.rule_receiver_client_id = :client_id")
    List<rule_receiver> findByClientId(@Param("client_id") int client_id);


    @Query("SELECT a FROM rule_receiver a WHERE a.rule_receiver_rule_id = :rule_id")
    List<rule_receiver> findByRuleId(@Param("rule_id") long rule_id);

   @Query("SELECT a FROM rule_receiver a WHERE a.rule_receiver_rule_id = :rule_id and a.rule_receiver_status='Active'")
   List<rule_receiver> findByRuleIdActive(@Param("rule_id") long rule_id);

    @Modifying
    @Transactional
    @Query("delete from rule_receiver b where b.rule_receiver_rule_id=:rule_id")
    void deleteByRuleId(@Param("rule_id") long rule_id);

    @Modifying
    @Transactional
    @Query("update rule_receiver b set b.rule_receiver_status=:status, b.rule_receiver_ref_name=:name where b.rule_receiver_ref_id=:person_id")
    void updateUserStatusName(@Param("person_id") long person_id,@Param("status") String status,@Param("name") String name);

}
