package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.Client;
import com.mncedicy.stims.api.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client,Integer> {

}
