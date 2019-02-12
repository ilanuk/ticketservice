package com.company.ticketservice.service;

import com.company.ticketservice.domain.SeatHold;
import com.company.ticketservice.repository.SeatHoldRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        int numSeats = 10;
        int freeSeatsAtStart = 10;
        String customerEmail = "abac@test.com";
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, Instant.now());
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
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, Instant.now());
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
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String confirmationCode = new String(array, Charset.forName("UTF-8"));
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, Instant.now());
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
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String confirmationCode = new String(array, Charset.forName("UTF-8"));
        SeatHold seatHold = new SeatHold(1, numSeats, customerEmail, Instant.now());
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
}