package com.company.ticketservice.repository;

import com.company.ticketservice.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SeatHoldRepositoryTest {
    private static Logger LOG = LoggerFactory
            .getLogger(SeatHoldRepositoryTest.class);
    SeatHoldRepository seatHoldRepository;
    private final int holdtime = 1000;

    Venue venue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        venue = new Venue(10, 10);
        seatHoldRepository = new SeatHoldRepository(venue, holdtime);
        final Map<Integer, SeatHold> seatHoldDatabase = new HashMap<>();
        final Map<Integer, SeatReserved> seatReservedDatabase = new HashMap<>();
        final AtomicInteger counter = new AtomicInteger();
    }


    @Test
    public void testAddSeatHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc1@acf.com";
        int numSeats2 = 6;
        String customerEmail2 = "abc2@acf.com";
        int numSeats3 = 7;
        String customerEmail3 = "abc3@acf.com";
        int numSeats4 = 8;
        String customerEmail4 = "abc4@acf.com";
        int numSeats5 = 9;
        String customerEmail5 = "abc5@acf.com";
        //when
        SeatHold seatHold1 = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        SeatHold seatHold3 = seatHoldRepository.addSeatHold(numSeats3, customerEmail3);
        SeatHold seatHold4 = seatHoldRepository.addSeatHold(numSeats4, customerEmail4);
        SeatHold seatHold5 = seatHoldRepository.addSeatHold(numSeats5, customerEmail5);
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(numSeats1, seatHold1.getNumberOfSeats());
        assertEquals(numSeats2, seatHold2.getNumberOfSeats());
        assertEquals(numSeats3, seatHold3.getNumberOfSeats());
        assertEquals(numSeats4, seatHold4.getNumberOfSeats());
        assertEquals(numSeats5, seatHold5.getNumberOfSeats());
        assertNotNull(seatHoldRepository.getSeatHold(seatHold1.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold2.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold3.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold4.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold5.getId()));
        assertEquals(numSeats1, seatHoldRepository.getSeatHold(seatHold1.getId()).getSeatsHeld().size());
        assertEquals(numSeats2, seatHoldRepository.getSeatHold(seatHold2.getId()).getSeatsHeld().size());
        assertEquals(numSeats3, seatHoldRepository.getSeatHold(seatHold3.getId()).getSeatsHeld().size());
        assertEquals(numSeats4, seatHoldRepository.getSeatHold(seatHold4.getId()).getSeatsHeld().size());
        assertEquals(numSeats5, seatHoldRepository.getSeatHold(seatHold5.getId()).getSeatsHeld().size());
        assertEquals(numSeats1+ numSeats2 + numSeats3 + numSeats4 + numSeats5, holdSeats);
        assertEquals(freeSeats, venue.getNumberOfFreeSeats());
        assertEquals(venue.getRows()*venue.getColumns()- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);
    }

    @Test
    public void testAddSeatReserved() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc1@acf.com";
        int numSeats2 = 6;
        String customerEmail2 = "abc2@acf.com";
        int numSeats3 = 7;
        String customerEmail3 = "abc3@acf.com";
        int numSeats4 = 8;
        String customerEmail4 = "abc4@acf.com";
        int numSeats5 = 9;
        String customerEmail5 = "abc5@acf.com";
        //when
        SeatHold seatHold1 = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        SeatHold seatHold3 = seatHoldRepository.addSeatHold(numSeats3, customerEmail3);
        SeatHold seatHold4 = seatHoldRepository.addSeatHold(numSeats4, customerEmail4);
        SeatHold seatHold5 = seatHoldRepository.addSeatHold(numSeats5, customerEmail5);
        String confirmationCode = seatHoldRepository.addSeatReserved(seatHold2.getId(), seatHold2.getCustomerEmail());
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        //then
        assertEquals(numSeats1, seatHold1.getNumberOfSeats());
        assertEquals(numSeats2, seatHold2.getNumberOfSeats());
        assertEquals(numSeats3, seatHold3.getNumberOfSeats());
        assertEquals(numSeats4, seatHold4.getNumberOfSeats());
        assertEquals(numSeats5, seatHold5.getNumberOfSeats());
        assertNotNull(seatHoldRepository.getSeatHold(seatHold1.getId()));
        assertNull(seatHoldRepository.getSeatHold(seatHold2.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold3.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold4.getId()));
        assertNotNull(seatHoldRepository.getSeatHold(seatHold5.getId()));
        assertEquals(numSeats1, seatHoldRepository.getSeatHold(seatHold1.getId()).getSeatsHeld().size());
        assertEquals(numSeats2, seatHoldRepository.getSeatReserved(confirmationCode).getSeatsReserved().size());
        assertEquals(numSeats3, seatHoldRepository.getSeatHold(seatHold3.getId()).getSeatsHeld().size());
        assertEquals(numSeats4, seatHoldRepository.getSeatHold(seatHold4.getId()).getSeatsHeld().size());
        assertEquals(numSeats5, seatHoldRepository.getSeatHold(seatHold5.getId()).getSeatsHeld().size());
        assertEquals(numSeats1 + numSeats3 + numSeats4 + numSeats5, holdSeats);
        assertEquals(freeSeats, venue.getNumberOfFreeSeats());
        assertEquals(venue.getRows()*venue.getColumns()- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

        assertNull(seatHoldRepository.getSeatHold(seatHold2.getId()));
        assertEquals(numSeats2, reservedSeats);
        assertNotNull(confirmationCode);
        assertNotNull(seatHoldRepository.getSeatReserved(confirmationCode));
        assertEquals(reservedSeats, seatHoldRepository.getSeatReserved(confirmationCode).getSeatsReserved().size());
        assertEquals(freeSeats, venue.getNumberOfFreeSeats());
        assertEquals(venue.getRows()*venue.getColumns()- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

    }

    @Test
    public void testFindSeatHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        Instant instant = Instant.now();
        //when
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        Optional<SeatHold> result = seatHoldRepository.findSeatHold(1);
        //then
        assertTrue(result.isPresent());
        assertEquals(seatHold.getId(), result.get().getId());
        assertEquals(seatHold.getCustomerEmail(), result.get().getCustomerEmail());
    }

    @Test
    public void getTotalSeatsHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        //when
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        String confirmationCode = seatHoldRepository.addSeatReserved(1, customerEmail1);
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(numSeats2, holdSeats);
        assertEquals(venue.getRows()*venue.getColumns() - seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

    }

    @Test
    public void getTotalSeatsReservedEmpty() {
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        assertEquals(0, reservedSeats);
    }

    @Test
    public void getTotalSeatsReservedWithHoldNonEmptyAfterReserved() {
        //given
        int numSeats1 = 15;
        String customerEmail1 = "abc@acf.com";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        //when
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        String confirmationCode = seatHoldRepository.addSeatReserved(seatHold.getId(), seatHold.getCustomerEmail());
        //then
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        assertNotNull(confirmationCode);
        assertEquals(numSeats1, reservedSeats);
        assertEquals(numSeats2, holdSeats);
    }

    @Test
    public void getTotalSeatsReservedWithHoldEmptyAfterReserved() {
        //given
        int numSeats1 = 15;
        String customerEmail1 = "abc@acf.com";
        //when
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        int holdSeatsbeforeReserve = seatHoldRepository.getTotalSeatsHold();
        String confirmationCode = seatHoldRepository.addSeatReserved(seatHold.getId(), seatHold.getCustomerEmail());
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        int holdSeatsAfterReserve = seatHoldRepository.getTotalSeatsHold();
        //then
        assertEquals(numSeats1, holdSeatsbeforeReserve);
        assertEquals(0, holdSeatsAfterReserve);
        assertNotNull(confirmationCode);
        assertEquals(numSeats1, reservedSeats);
    }
    @Test
    public void getTotalSeatsReservedWithIncorrectEmailFromHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String customerEmailIncorrect1 = "babc@acf.com";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        //when
        int freeSeatsInitial = seatHoldRepository.getTotalSeatsFree();
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        int freeSeatsAfterFirstHold = seatHoldRepository.getTotalSeatsFree();
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        int freeSeatsAfterSecondHold = seatHoldRepository.getTotalSeatsFree();

        int holdSeatsBeforeReserved = seatHoldRepository.getTotalSeatsHold();
        String confirmationCode = seatHoldRepository.addSeatReserved(seatHold.getId(), customerEmailIncorrect1);
        int freeSeatsAfterReserve = seatHoldRepository.getTotalSeatsFree();
        //then
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        int holdSeatsAfterReserved = seatHoldRepository.getTotalSeatsHold();
        int venueCapacity = venue.getRows()*venue.getColumns();
        assertEquals(0, reservedSeats);
        assertNotEquals(5, reservedSeats);
        assertNull(confirmationCode);

        assertEquals(numSeats1+numSeats2 , holdSeatsBeforeReserved);
        assertEquals(numSeats1+numSeats2 , holdSeatsAfterReserved);
        assertEquals(venueCapacity, freeSeatsInitial);
        assertEquals(venueCapacity - numSeats1, freeSeatsAfterFirstHold);
        assertEquals(venueCapacity - numSeats1 - numSeats2, freeSeatsAfterSecondHold);
        assertEquals(venueCapacity - numSeats1 - numSeats2, freeSeatsAfterReserve);
    }

    @Test
    public void testGetTotalSeatsFree() {
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        int venueCapacity = venue.getRows()*venue.getColumns();
        assertEquals(venueCapacity, freeSeats);
    }

    @Test
    public void getConfirmationCode() {
        //given
        String confirmationCode = null;
        //when
        confirmationCode = seatHoldRepository.getConfirmationCode();
        //then
        assertNotNull(confirmationCode);
    }

    @Test
    public void testExpireSeatHolds() throws Exception {
        //given
        int capacity = venue.getRows()*venue.getColumns();
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        int numSeats2 = 15;
        String customerEmail2 = "abc2@acf.com";
        //when
        int freeSeatsInitial = seatHoldRepository.getTotalSeatsFree();
        SeatHold seatHold = seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        SeatHold seatHold2 = seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        int freeSeatsBefore = seatHoldRepository.getTotalSeatsFree();
        try {
            //simulate delay
            Thread.sleep(4000);
            seatHoldRepository.expireSeatHolds();
        }
        catch (Exception e) {
            throw e;
        }
        int freeSeatsAfter = seatHoldRepository.getTotalSeatsFree();
        assertEquals(capacity, freeSeatsInitial);
        assertNotEquals(freeSeatsInitial, freeSeatsBefore);
        assertNotEquals(freeSeatsAfter, freeSeatsBefore);
        assertEquals(freeSeatsInitial, freeSeatsAfter);
    }
}