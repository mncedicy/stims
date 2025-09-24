package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.enatis_make;
import com.mncedicy.stims.api.Model.enatis_usage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface enatis_usageRepo extends JpaRepository<enatis_usage,Integer> {

}
