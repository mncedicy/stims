package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Privilege;
import com.mncedicy.stims.api.Model.Role;
import com.mncedicy.stims.api.Model.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role,Integer> {

    @Query("SELECT a FROM Role a WHERE a.role_client_id = :client_id")
    List<Role> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM Role a WHERE a.role_client_id = :client_id and a.role_status = 'Active'")
    List<Role> findActiveByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM Role a WHERE a.role_client_id = :client_id and a.role_name = :role_name")
    List<Role> findByClientIdAndName(@Param("client_id") int client_id,@Param("role_name") String role_name);
}
