package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.vehicle_make;
import com.mncedicy.stims.api.Model.vehicle_usage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleUsageRepo extends JpaRepository<vehicle_usage,Integer> {

}
