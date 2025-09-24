package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.charge_code;
import com.mncedicy.stims.api.Model.court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourtRepo extends JpaRepository<court,Integer> {

}
