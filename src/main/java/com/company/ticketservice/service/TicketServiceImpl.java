package com.company.ticketservice.service;

import com.company.ticketservice.TicketserviceApplication;
import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.repository.SeatHoldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService{
    private static Logger LOG = LoggerFactory
            .getLogger(TicketServiceImpl.class);

    final SeatHoldRepository seatHoldRepository;

    @Autowired
    TicketServiceImpl(SeatHoldRepository seatHoldRepository) {
        this.seatHoldRepository = seatHoldRepository;
    }

    @Override
    public int numSeatsAvailable() {
        return seatHoldRepository.getTotalSeatsFree();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        return seatHoldRepository.addSeatHold(numSeats, customerEmail);
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return seatHoldRepository.addSeatReserved(seatHoldId, customerEmail);
    }
}
