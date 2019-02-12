package com.company.ticketservice.repository;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.domain.SeatReserved;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SeatHoldRepositoryTest {

    SeatHoldRepository seatHoldRepository;
    private final int capacity = 100;
    private final int holdtime = 1000;

    @Before
    public void setup() {
        seatHoldRepository = new SeatHoldRepository(capacity, holdtime);
        final Map<Integer, SeatHold> seatHoldDatabase = new HashMap<>();
        final Map<Integer, SeatReserved> seatReservedDatabase = new HashMap<>();
        final AtomicInteger counter = new AtomicInteger();
    }


    @Test
    public void testAddSeatHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        //when
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        seatHoldRepository.addSeatReserved(1, customerEmail1);
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(numSeats1 + numSeats2, holdSeats);
        assertEquals(capacity- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

    }

    @Test
    public void testAddSeatReserved() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 15;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        //when
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        seatHoldRepository.addSeatReserved(1, customerEmail1);
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(numSeats1, reservedSeats);
        assertEquals(capacity- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

    }

    @Test
    public void testFindSeatHold() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        Instant instant = Instant.now();
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, instant);
        //when
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
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
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        //when
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        seatHoldRepository.addSeatReserved(1, customerEmail1);
        int holdSeats = seatHoldRepository.getTotalSeatsHold();
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(numSeats1 + numSeats2, holdSeats);
        assertEquals(capacity- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);

    }

    @Test
    public void getTotalSeatsReservedEmpty() {
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        assertEquals(0, reservedSeats);
    }

    @Test
    public void getTotalSeatsReservedWithNonEmpty() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 5;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        SeatReserved seatReserved = new SeatReserved(1, numSeats1, customerEmail1, Instant.now(), confirmationCode1);
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        seatHoldRepository.addSeatReserved(1, customerEmail1);
        int reservedSeats = seatHoldRepository.getTotalSeatsReserved();
        assertEquals(5, reservedSeats);
    }
    @Test
    public void testGetTotalSeatsFree() {
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        assertEquals(capacity, freeSeats);
    }

    @Test
    public void testGetTotalSeatsFreeAfterTwoSeatHolds() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 15;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        //when
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(capacity- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);
    }

    @Test
    public void testGetTotalSeatsFreeAfterTwoSeatHoldsOneReserve() {
        //given
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 15;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        SeatReserved seatReserved = new SeatReserved(1, numSeats1, customerEmail1, Instant.now(), confirmationCode1);
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        seatHoldRepository.addSeatReserved(1, customerEmail1);
        //when
        int freeSeats = seatHoldRepository.getTotalSeatsFree();
        //then
        assertEquals(capacity- seatHoldRepository.getTotalSeatsHold() - seatHoldRepository .getTotalSeatsReserved(), freeSeats);
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
        int numSeats1 = 5;
        String customerEmail1 = "abc@acf.com";
        String confirmationCode1 = "ASDFGH7";
        int numSeats2 = 15;
        String customerEmail2 = "abc2@acf.com";
        String confirmationCode2 = "ASD2FGH";
        SeatHold seatHold = new SeatHold(1, numSeats1, customerEmail1, Instant.now());
        SeatHold seatHold2 = new SeatHold(2, numSeats2, customerEmail2, Instant.now());
        SeatReserved seatReserved = new SeatReserved(1, numSeats1, customerEmail1, Instant.now(), confirmationCode1);
        //when
        int freeSeatsInitial = seatHoldRepository.getTotalSeatsFree();
        seatHoldRepository.addSeatHold(numSeats1, customerEmail1);
        seatHoldRepository.addSeatHold(numSeats2, customerEmail2);
        int freeSeatsBefore = seatHoldRepository.getTotalSeatsFree();
        try {
            //simulate scheduler delay
            Thread.sleep(2000);
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