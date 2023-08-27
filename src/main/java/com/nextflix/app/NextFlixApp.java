package com.nextflix.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJdbcHttpSession
@EnableScheduling
public class NextFlixApp {

	public static void main(String[] args) {
		SpringApplication.run(NextFlixApp.class, args);
	}

}
