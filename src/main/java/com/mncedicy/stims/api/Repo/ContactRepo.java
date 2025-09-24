package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Contact;
import com.mncedicy.stims.api.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact,Long> {

}
