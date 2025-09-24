package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.enatis_model;
import com.mncedicy.stims.api.Model.enatis_type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface enatis_modelRepo extends JpaRepository<enatis_model,Integer> {

}
