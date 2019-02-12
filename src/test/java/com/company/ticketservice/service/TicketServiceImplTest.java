package com.company.ticketservice.service;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.domain.Venue;
import com.company.ticketservice.repository.SeatHoldRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class TicketServiceImplTest {

    TicketServiceImpl ticketServiceImpl;

    @Mock
    SeatHoldRepository seatHoldRepository;

    Venue venue;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        venue = new Venue(10,10);
        ticketServiceImpl = new TicketServiceImpl(seatHoldRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNumSeatsAvailableWithEmptyHoldandReserved() {
        //given
        int freeSeatsAtStart = 10;
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        //When
        int result = ticketServiceImpl.numSeatsAvailable();
        //Then
        assertEquals(result, freeSeatsAtStart);
    }

    @Test
    public void testFindAndHoldSeatsWithEnoughFreeSeats() {
        //given
        int numSeats = 7;
        int freeSeatsAtStart = 10;
        String customerEmail = "abac@test.com";
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, venue.getFreeSeats(numSeats).get(), Instant.now());
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart - numSeats);
        given(seatHoldRepository.addSeatHold(numSeats, customerEmail) ).will(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if((freeSeatsAtStart - numSeats)>=0) {
                    return seatHold;
                }
                else {
                    return null;
                }
            }
        });

        //when
        SeatHold result = ticketServiceImpl.findAndHoldSeats(numSeats, customerEmail);
        //then
        assertNotNull(result);
        assertTrue((freeSeatsAtStart - numSeats)>=0);
        assertEquals((freeSeatsAtStart - numSeats), seatHoldRepository.getTotalSeatsFree());
    }

    @Test
    public void testFindAndHoldSeatsWithNotEnoughFreeSeats() {
        //given
        int numSeats = 10;
        int freeSeatsAtStart = 9;
        String customerEmail = "abac@test.com";
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, venue.getFreeSeats(numSeats).get(), Instant.now());
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart - numSeats);
        given(seatHoldRepository.addSeatHold(numSeats, customerEmail) ).will(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if((freeSeatsAtStart - numSeats)>=0) {
                    return seatHold;
                }
                else {
                    return null;
                }
            }
        });

        //when
        SeatHold result = ticketServiceImpl.findAndHoldSeats(numSeats, customerEmail);
        //then
        assertNull(result);
        assertTrue((freeSeatsAtStart - numSeats)<0);
        assertEquals((freeSeatsAtStart - numSeats), seatHoldRepository.getTotalSeatsFree());
    }


    @Test
    public void testReserveSeatsWithHoldSeats() {
        //given
        int numSeats = 10;
        int freeSeatsAtStart = 10;
        String customerEmail = "abac@test.com";
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String confirmationCode = RandomStringUtils.random(length, useLetters, useNumbers);
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, venue.getFreeSeats(numSeats).get(), Instant.now());
        Optional<SeatHold> seatHoldOptional = Optional.of(seatHold);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        given(seatHoldRepository.getConfirmationCode()).willReturn(confirmationCode);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart - numSeats);
        given(seatHoldRepository.findSeatHold(seatHold.getId())).willReturn(seatHoldOptional);
        given(seatHoldRepository.addSeatReserved(seatHold.getId(), customerEmail) ).will(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if(seatHoldOptional.isPresent()) {
                    return confirmationCode;
                }
                else {
                    return null;
                }
            }
        });

        //when
        String result = ticketServiceImpl.reserveSeats(seatHold.getId(), customerEmail);
        //then
        assertNotNull(result);
        assertEquals(confirmationCode, result);
    }


    @Test
    public void testReserveSeatsWithoutHoldSeats() {
        //given
        int numSeats = 10;
        int freeSeatsAtStart = 10;
        String customerEmail = "abac@test.com";
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String confirmationCode = RandomStringUtils.random(length, useLetters, useNumbers);
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, venue.getFreeSeats(numSeats).get(), Instant.now());
        Optional<SeatHold> seatHoldOptional = Optional.empty();
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        given(seatHoldRepository.getConfirmationCode()).willReturn(confirmationCode);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart - numSeats);
        given(seatHoldRepository.findSeatHold(seatHold.getId())).willReturn(seatHoldOptional);
        given(seatHoldRepository.addSeatReserved(seatHold.getId(), customerEmail) ).will(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if(seatHoldOptional.isPresent()) {
                    return confirmationCode;
                }
                else {
                    return null;
                }
            }
        });

        //when
        String result = ticketServiceImpl.reserveSeats(seatHold.getId(), customerEmail);
        //then
        assertNull(result);
        assertNotEquals(confirmationCode, result);
    }

    @Test
    public void testReserveSeatsWithIncorrectHoldSeatEmail() {
        //given
        int numSeats = 10;
        int freeSeatsAtStart = 10;
        String customerEmail = "abac@test.com";
        String customerEmailIncorrect = "babac@test.com";
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = false;
        String confirmationCode = RandomStringUtils.random(length, useLetters, useNumbers);
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, venue.getFreeSeats(numSeats).get(), Instant.now());
        Optional<SeatHold> seatHoldOptional = Optional.of(seatHold);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart);
        given(seatHoldRepository.getConfirmationCode()).willReturn(confirmationCode);
        given(seatHoldRepository.getTotalSeatsFree()).willReturn(freeSeatsAtStart - numSeats);
        given(seatHoldRepository.findSeatHold(seatHold.getId())).willReturn(seatHoldOptional);
        given(seatHoldRepository.addSeatReserved(seatHold.getId(), customerEmailIncorrect) ).will(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String emailArg = (String)args[1];
                if(seatHoldOptional.isPresent() && seatHoldOptional.get().getCustomerEmail().equalsIgnoreCase(emailArg)) {
                    return confirmationCode;
                }
                else {
                    return null;
                }
            }
        });

        //when
        String result = ticketServiceImpl.reserveSeats(seatHold.getId(), customerEmailIncorrect);
        //then
        assertNull(result);
        assertNotEquals(confirmationCode, result);
    }
}