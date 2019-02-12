package com.company.ticketservice.repository;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.domain.SeatReserved;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SeatHoldRepository {
    private final Map<Integer, SeatHold> seatHoldDatabase = new HashMap<>();
    private final Map<Integer, SeatReserved> seatReservedDatabase = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();
    private final int capacity;
    private final int holdtime;

    @Autowired
    SeatHoldRepository(@Value("${seat.capacity}") final int capacity,
                       @Value("${seat.holdtimedelay}") final int holdtime) {
        this.capacity = capacity;
        this.holdtime = holdtime;
    }

    //addSeatHold
    public SeatHold addSeatHold(int numSeats, String customerEmail) {
        long freeSeats = getTotalSeatsFree();
        SeatHold seatHoldout = null;
        if (freeSeats >= numSeats) {
            seatHoldout = new SeatHold(counter.incrementAndGet(), numSeats, customerEmail, Instant.now());
            seatHoldDatabase.put(seatHoldout.getId(), seatHoldout);
        }
        return seatHoldout;
    }

    //addSeatReserved
    public String addSeatReserved(int seatHoldId, String customerEmail) {

        Optional<SeatHold> seatHoldOptional = findSeatHold(seatHoldId);
        String confirmationCode = null;
        if (seatHoldOptional.isPresent()) {
            confirmationCode = getConfirmationCode();
            SeatHold seatHold = seatHoldOptional.get();
            SeatReserved seatReserved = new SeatReserved(seatHoldId,
                    seatHold.getNumberOfSeats(),
                    seatHold.getCustomerEmail(),
                    Instant.now(),
                    confirmationCode);
            seatHoldDatabase.remove(seatHold);
            seatReservedDatabase.put(seatHoldId, seatReserved);
        }
        return confirmationCode;
    }

    //findSeatHold
    public Optional<SeatHold> findSeatHold(final int seatId) {
        return Optional.of(seatHoldDatabase.get(seatId));
    }

    public int getTotalSeatsHold() {
        return seatHoldDatabase.values().stream().map(e -> (e.getNumberOfSeats())).mapToInt(Integer::intValue).sum();
    }

    public int getTotalSeatsReserved() {
        return seatReservedDatabase.values().stream().map(e -> (e.getNumberOfSeats())).mapToInt(Integer::intValue).sum();
    }

    public int getTotalSeatsFree() {
        int numHold = getTotalSeatsHold();
        int numReserved = getTotalSeatsReserved();
        return (capacity - numHold - numReserved);
    }

    public String getConfirmationCode() {
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    @Scheduled(fixedDelayString = "${seat.schedulefrequency}")
    public void expireSeatHolds() {
        List<SeatHold> keys = seatHoldDatabase.values().stream().collect(Collectors.toList());
        for (SeatHold s : keys)
        {
            Instant instantNow = Instant.now();
            Instant holdStart = s.getInstant();
            if(holdStart.plusMillis(holdtime).isBefore(instantNow)) {
                seatHoldDatabase.remove(s.getId());
            }
        }
    }
}
