package com.company.ticketservice.domain;

import lombok.*;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SeatReserved {
    private final int id;
    private final int numberOfSeats;
    private final String customerEmail;
    private final String confirmationCode;
    private final List<Seat> seatsReserved;
    private final Instant instantReserved;
}
