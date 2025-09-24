package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.vehicle_make;
import com.mncedicy.stims.api.Model.vehicle_type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepo extends JpaRepository<vehicle_type,Integer> {

}
