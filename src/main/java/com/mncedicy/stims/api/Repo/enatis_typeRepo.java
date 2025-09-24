package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.enatis_type;
import com.mncedicy.stims.api.Model.enatis_usage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface enatis_typeRepo extends JpaRepository<enatis_type,Integer> {

}
