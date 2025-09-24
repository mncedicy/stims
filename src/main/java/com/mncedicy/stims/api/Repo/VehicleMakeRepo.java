package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.book;
import com.mncedicy.stims.api.Model.vehicle_make;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleMakeRepo extends JpaRepository<vehicle_make,Integer> {

}
