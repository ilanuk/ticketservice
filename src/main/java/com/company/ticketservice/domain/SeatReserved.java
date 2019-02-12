package com.company.ticketservice.domain;

import lombok.*;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SeatReserved {
    private final int id;
    private final int numberOfSeats;
    private final String customerEmail;
    private final Instant instant;
    private final String confirmationCode;

}
