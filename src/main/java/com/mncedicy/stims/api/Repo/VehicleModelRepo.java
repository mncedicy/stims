package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.vehicle_make;
import com.mncedicy.stims.api.Model.vehicle_model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleModelRepo extends JpaRepository<vehicle_model,Integer> {

}
