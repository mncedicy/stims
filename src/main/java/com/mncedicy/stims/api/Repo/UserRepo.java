package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Role;
import com.mncedicy.stims.api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Integer> {


    @Query("SELECT a FROM User a WHERE LOWER(a.user_username) = LOWER(:user_username)")
    List<User> findUserByUsername(@Param("user_username") String user_username);

    @Query("SELECT a FROM User a WHERE LOWER(a.user_username) = LOWER(:user_username) and a.user_password = :user_password")
    List<User> findUserByUsernameAndPassword(@Param("user_username") String user_username,@Param("user_password") String user_password);

    @Query("SELECT a FROM User a WHERE a.user_client_id = :client_id")
    List<User> findByClientId(@Param("client_id") int client_id);

    @Query("SELECT a FROM User a WHERE a.user_client_id = :client_id and a.user_status = 'Active'")
    List<User> findByClientIdActive(@Param("client_id") int client_id);

    @Query("SELECT a FROM User a WHERE a.user_client_id = :client_id and a.user_role_id = :role_id")
    List<User> findByClientIdAndRole(@Param("client_id") int client_id,@Param("role_id") int role_id);



}
