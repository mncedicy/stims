package com.mncedicy.stims.api.Repo;

import com.mncedicy.stims.api.Model.vehicle_colour;
import com.mncedicy.stims.api.Model.vehicle_make;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleColourRepo extends JpaRepository<vehicle_colour,Integer> {

}
