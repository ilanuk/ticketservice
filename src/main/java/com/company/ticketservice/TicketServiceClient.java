package com.company.ticketservice;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

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
        SeatHold seatHold1 = ticketService.findAndHoldSeats(numSeats1, email1);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 1 hold = " + numOfSeatsAvailable);
        LOG.info("Going to reserve the hold :"+ seatHold1);
        String confirmCode = ticketService.reserveSeats(seatHold1.getId(), seatHold1.getCustomerEmail());
        LOG.info("Confirmation code from reserving the reserve 1 =" + confirmCode);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after reserve = " + numOfSeatsAvailable);
        int numSeats2 = 15;
        String email2 = "ilan2@testing.com";
        LOG.info("Going to hold " + numSeats2 + "seats with customer email : "+ email2);
        SeatHold seatHold2 = ticketService.findAndHoldSeats(numSeats2, email2);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 2 hold = " + numOfSeatsAvailable);
        LOG.info("Starting to wait for " + 5 + "seconds.");
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            throw e;
        }
        LOG.info("After waiting for " + 5 + "seconds.");
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 2 hold and waiting = " + numOfSeatsAvailable);

        int numSeats3 = 3;
        String email3 = "ilan3@testing.com";
        LOG.info("Going to hold " + numSeats3 + "seats with customer email : "+ email3);
        SeatHold seatHold3 = ticketService.findAndHoldSeats(numSeats3, email3);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 3 hold = " + numOfSeatsAvailable);

        int numSeats4 = 4;
        String email4 = "ilan4@testing.com";
        LOG.info("Going to hold " + numSeats4 + "seats with customer email : "+ email4);
        SeatHold seatHold4 = ticketService.findAndHoldSeats(numSeats4, email4);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 4 hold = " + numOfSeatsAvailable);

        int numSeats5 = 5;
        String email5 = "ilan5@testing.com";
        LOG.info("Going to hold " + numSeats5 + "seats with customer email : "+ email5);
        SeatHold seatHold5 = ticketService.findAndHoldSeats(numSeats5, email5);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 5 hold = " + numOfSeatsAvailable);

        int numSeats6 = 6;
        String email6 = "ilan6@testing.com";
        LOG.info("Going to hold " + numSeats6 + "seats with customer email : "+ email6);
        SeatHold seatHold6 = ticketService.findAndHoldSeats(numSeats6, email6);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 6 hold = " + numOfSeatsAvailable);

        LOG.info("Going to reserve the hold :"+ seatHold6);
        String confirmCode6 = ticketService.reserveSeats(seatHold6.getId(), seatHold6.getCustomerEmail());
        LOG.info("Confirmation code from reserving the reserve 6 =" + confirmCode6);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after reserve 6= " + numOfSeatsAvailable);

        LOG.info("Going to reserve the hold :"+ seatHold3);
        String confirmCode3 = ticketService.reserveSeats(seatHold3.getId(), seatHold3.getCustomerEmail());
        LOG.info("Confirmation code from reserving the reserve 3 =" + confirmCode3);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after reserve 3= " + numOfSeatsAvailable);

        LOG.info("Starting to wait for " + 5 + "seconds.");
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {
            throw e;
        }
        LOG.info("After waiting for " + 5 + "seconds.");
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after waiting = " + numOfSeatsAvailable);

        int numSeats7 = 81;
        String email7 = "ilan81@testing.com";
        LOG.info("Going to hold " + numSeats7 + "seats with customer email : "+ email7);
        SeatHold seatHold7 = ticketService.findAndHoldSeats(numSeats7, email7);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 7 hold = " + numOfSeatsAvailable);

        LOG.info("Going to reserve the hold :"+ seatHold7);
        String confirmCode4 = ticketService.reserveSeats(seatHold7.getId(), seatHold7.getCustomerEmail());
        LOG.info("Confirmation code from reserving the reserve 4 =" + confirmCode4);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after reserve 4= " + numOfSeatsAvailable);

        int numSeats8 = 82;
        String email8 = "ilan82@testing.com";
        LOG.info("Going to hold " + numSeats8 + "seats with customer email : "+ email8);
        SeatHold seatHold8 = ticketService.findAndHoldSeats(numSeats8, email8);
        numOfSeatsAvailable = ticketService.numSeatsAvailable();
        LOG.info("Number of Seats available now after 8 hold = " + numOfSeatsAvailable);

    }
}
