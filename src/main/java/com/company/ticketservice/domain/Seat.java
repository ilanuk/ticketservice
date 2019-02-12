package com.company.ticketservice.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Seat {
    private final int id;
    private final int rowNum;
    private final int columnNum;
    private SeatStatus seatStatus;
    public Seat(int id, int rowNum, int columnNum, SeatStatus seatStatus) {
        this.id = id;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.seatStatus = seatStatus;
    }
}
