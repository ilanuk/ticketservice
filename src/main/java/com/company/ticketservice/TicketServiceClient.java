package com.company.ticketservice;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TicketServiceClient implements CommandLineRunner {
    private static Logger LOG = LoggerFactory
            .getLogger(TicketServiceClient.class);

    TicketService ticketService;

    @Autowired
    TicketServiceClient(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("STARTING THE TICKET SERVICE CLIENT");
        int numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now " + numOfSeatsAvailable);
        int numSeats1 = 10;
        String email1 = "ilan1@testing.com";
        LOG.info("Going to hold " + numSeats1 + "seats with customer email : "+ email1);
        SeatHold seatHold = ticketService.findAndHoldSeats(numSeats1, email1);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 1 hold = " + numOfSeatsAvailable);
        LOG.info("Going to reserve the hold :"+ seatHold);
        String confirmCode = ticketService.reserveSeats(seatHold.getId(), seatHold.getCustomerEmail());
        LOG.info("Confirmation code from reserving the hold =" + confirmCode);
        numSeats1 = 15;
        email1 = "ilan2@testing.com";
        LOG.info("Going to hold " + numSeats1 + "seats with customer email : "+ email1);
        seatHold = ticketService.findAndHoldSeats(numSeats1, email1);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 1 hold = " + numOfSeatsAvailable);
        LOG.info("Starting to wait for " + 5 + "seconds.");
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            throw e;
        }
        LOG.info("After waiting for " + 5 + "seconds.");
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 1 hold = " + numOfSeatsAvailable);

    }
}
