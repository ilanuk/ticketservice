package com.company.ticketservice.repository;

import com.company.ticketservice.domain.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SeatHoldRepository {
    private static Logger LOG = LoggerFactory
            .getLogger(SeatHoldRepository.class);

    private final Map<Integer, SeatHold> seatHoldDatabase = new HashMap<>();
    private final Map<Integer, SeatReserved> seatReservedDatabase = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();
    private final int holdtime;
    private final Venue venue;

    @Autowired
    public SeatHoldRepository(Venue venue,
                       @Value("${seat.holdtimedelay}") final int holdtime) {
        this.holdtime = holdtime;
        this.venue = venue;

    }

    //addSeatHold
    public synchronized SeatHold addSeatHold(int numSeats, String customerEmail) {
        expireSeatHolds();
        long freeSeats = getTotalSeatsFree();
        SeatHold seatHoldout = null;
        if (freeSeats >= numSeats) {
            Optional<List<Seat>> listOfSeats = venue.getFreeSeats(numSeats);
            listOfSeats.get().stream().forEach(e->e.setSeatStatus(SeatStatus.HOLD));
            seatHoldout = new SeatHold(counter.incrementAndGet(), numSeats, customerEmail, listOfSeats.get(), Instant.now().plusMillis(holdtime));
            seatHoldDatabase.put(seatHoldout.getId(), seatHoldout);
        }
        return seatHoldout;
    }

    //addSeatReserved
    public synchronized String addSeatReserved(int seatHoldId, String customerEmail) {
        expireSeatHolds();
        Optional<SeatHold> seatHoldOptional = findSeatHold(seatHoldId);
        String confirmationCode = null;
        if (seatHoldOptional.isPresent() && seatHoldOptional.get().getCustomerEmail().equalsIgnoreCase(customerEmail)) {
            confirmationCode = getConfirmationCode();
            SeatHold seatHold = seatHoldDatabase.get(seatHoldId);
            List<Seat> seatsHeld = seatHold.getSeatsHeld();
            seatsHeld.stream().forEach(ss -> ss.setSeatStatus(SeatStatus.RESERVED));
            SeatReserved seatReserved = new SeatReserved(seatHoldId,
                    seatHold.getNumberOfSeats(),
                    seatHold.getCustomerEmail(),
                    confirmationCode,
                    seatsHeld,
                    Instant.now()
            );
            seatHoldDatabase.remove(seatHoldId);
            seatReservedDatabase.put(seatHoldId, seatReserved);
        }
        return confirmationCode;
    }

    //findSeatHold
    public Optional<SeatHold> findSeatHold(final int seatId) {
        return Optional.of(seatHoldDatabase.get(seatId));
    }

    public int getTotalSeatsHold() {
        return seatHoldDatabase.values().stream()
                .filter(e -> e.getInstantEndHold().isAfter(Instant.now()))
                .map(e -> (e.getNumberOfSeats())).mapToInt(Integer::intValue).sum();
    }

    public int getTotalSeatsReserved() {
        return seatReservedDatabase.values().stream().map(e -> (e.getNumberOfSeats())).mapToInt(Integer::intValue).sum();
    }

    public int getTotalSeatsFree() {
        expireSeatHolds();
        int numHold = getTotalSeatsHold();
        int numReserved = getTotalSeatsReserved();
        return (venue.getRows() * venue.getColumns() - numHold - numReserved);
    }

    public String getConfirmationCode() {
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public synchronized void expireSeatHolds() {
        List<SeatHold> keys = seatHoldDatabase.values().stream().collect(Collectors.toList());
        for (SeatHold s : keys) {
            Instant instantNow = Instant.now();
            Instant holdEnd = s.getInstantEndHold();
            if (holdEnd.isBefore(instantNow)) {
                s.getSeatsHeld().stream().forEach(e -> e.setSeatStatus(SeatStatus.AVAILABLE));
                seatHoldDatabase.remove(s.getId());
            }
        }
    }

    public SeatHold getSeatHold(int seatHoldId) {
        return seatHoldDatabase.get(seatHoldId);
    }

    public SeatReserved getSeatReserved(String confirmationCode) {
        return seatReservedDatabase.values()
                .stream()
                .filter(e->e.getConfirmationCode().equalsIgnoreCase(confirmationCode))
                .collect(Collectors.toList())
                .get(0);
    }}
