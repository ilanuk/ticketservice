package com.company.ticketservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class TicketserviceApplication {
	private static Logger LOG = LoggerFactory
			.getLogger(TicketserviceApplication.class);
	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION  TICKET SERVICE");
		SpringApplication.run(TicketserviceApplication.class, args);
		LOG.info("FINISHED THE APPLICATION TICKET SERVICE");
	}

}

