package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Person;
import com.mncedicy.stims.api.Model.User;
import com.mncedicy.stims.api.Model.infringement_notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepo extends JpaRepository<Person,Long> {
    @Query("SELECT a FROM Person a WHERE a.person_id_number = :id_number")
    List<Person> findByIdNumber(@Param("id_number") String id_number);

}
