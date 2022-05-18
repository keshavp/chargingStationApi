package com.scube.chargingstation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scube.chargingstation.util.FileStorageProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition
@EnableConfigurationProperties({
	FileStorageProperties.class
})
@SecurityScheme(name = "javainuseapi", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ChargingStationApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ChargingStationApiApplication.class, args);
	}


}
