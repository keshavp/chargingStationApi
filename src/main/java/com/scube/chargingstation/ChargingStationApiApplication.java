package com.scube.chargingstation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scube.chargingstation.util.FileStorageProperties;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class ChargingStationApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ChargingStationApiApplication.class, args);
	}


}
