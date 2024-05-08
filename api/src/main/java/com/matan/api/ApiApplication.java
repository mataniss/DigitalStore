package com.matan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.matan.api.managers.DBManager;
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		DBManager.connectToDB();
		SpringApplication.run(ApiApplication.class, args);

	}

}
