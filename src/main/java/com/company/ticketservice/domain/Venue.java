package com.company.ticketservice.domain;

import com.company.ticketservice.repository.SeatHoldRepository;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Component
public class Venue {
    private static Logger LOG = LoggerFactory
            .getLogger(Venue.class);
    private final int rows;
    private final int columns;
    private final List<List<Seat>> seats;
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    public Venue(@Value("${venue.rows}") final int rows,
          @Value("${venue.rows}") final int columns) {
        this.rows = rows;
        this.columns = columns;
        this.seats = new ArrayList<List<Seat>>(this.rows);
        initializeState(seats);
    }

    private void initializeState(List<List<Seat>> seats) {
        for (int i = 0; i < this.rows ; i++) {
            List<Seat> myList = new ArrayList<Seat>(this.columns);
            seats.add(i, myList);
            for (int j = 0; j < columns ; j++) {
                int idVal = counter.incrementAndGet();
                Seat seat = new Seat(idVal, i, j, SeatStatus.AVAILABLE);
                seats.get(i).add(j, seat);
            }
        }
    }

    public synchronized void setSeatState(Seat seat, SeatStatus seatStatus) {
        if( (seatStatus.equals(SeatStatus.AVAILABLE) || seatStatus.equals(SeatStatus.RESERVED))
                && seat.getSeatStatus().equals(SeatStatus.HOLD) ) {
            seats.get(seat.getRowNum()).get(seat.getColumnNum()).setSeatStatus(seatStatus);
        }
    }

    public long getNumberOfFreeSeats() {
        long freeSeats = seats.stream()
                .flatMap(e -> e.stream())
                .filter(f-> f.getSeatStatus().equals(SeatStatus.AVAILABLE))
                .count();
        return freeSeats;
    }

    public long getNumberOfHoldSeats() {
        long holdSeats = seats.stream()
                .flatMap(e -> e.stream())
                .filter(f-> f.getSeatStatus().equals(SeatStatus.HOLD))
                .count();
        return holdSeats;
    }

    public long getNumberOfReservedSeats() {
        long reservedSeats = seats.stream()
                .flatMap(e -> e.stream())
                .filter(f-> f.getSeatStatus().equals(SeatStatus.RESERVED))
                .count();
        return reservedSeats;
    }

    public Optional<List<Seat>> getFreeSeats(int numOfSeats) {
        List<Seat> seatList = seats.stream()
                .flatMap(e -> e.stream())
                .filter(f-> f.getSeatStatus().equals(SeatStatus.AVAILABLE))
                .limit(numOfSeats)
                .collect(Collectors.toList());
                ;
        return Optional.of(seatList);
    }
}
