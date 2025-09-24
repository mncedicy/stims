package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.RolePrivilege;
import com.mncedicy.stims.api.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePrivRepo extends JpaRepository<RolePrivilege,Long> {

    @Query("SELECT a FROM RolePrivilege a WHERE a.role_privilege_role_id = :role_id")
    List<RolePrivilege> findByRoleId(@Param("role_id") int role_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM RolePrivilege a WHERE a.role_privilege_role_id = :role_id")
    void deleteByRoleId(@Param("role_id") int role_id);

}
