package com.mncedicy.stims.api;

import com.mncedicy.stims.api.Controller.ConfigurationController;
import com.mncedicy.stims.api.Controller.ManagementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SuppressWarnings("ALL")
@SpringBootApplication
@EnableScheduling

public class ApiApplication {

	@Autowired
	ManagementController managementController;
	@Autowired
	ConfigurationController configurationController;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Scheduled(cron = "0 0 06 * * *")
	public void runEveyDayAt6() {
		configurationController.runAutomations("");
	}

}
