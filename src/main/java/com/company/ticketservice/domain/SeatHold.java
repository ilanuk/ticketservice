package com.company.ticketservice.domain;

import lombok.*;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SeatHold {
    private final int id;
    private final int numberOfSeats;
    private final String customerEmail;
    private final List<Seat> seatsHeld;
    private final Instant instantEndHold;
}
