package org.example;

import org.example.controllers.ShowController;
import org.example.exceptions.*;
import org.example.models.Ticket;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TestCancelBooking {
    ShowController controller;
    Ticket ticket;
    @Before
    public void init() throws InvalidArgumentException, ShowAlreadyExistsException, ShowNotFoundException, RowNotFoundException, SeatNotFoundException {
        controller = new ShowController();
        controller.setupShow(1, 10, 10, 1);
        this.ticket = controller.bookSeats(1,"1234", "A1").get(0);
    }
    @Test
    public void cancelBookingValid() throws InvalidArgumentException, ShowNotFoundException {
        assertTrue(controller.cancelBooking(this.ticket.getNumber(), "1234"));
    }
    @Test
    public void cancelBookingInvalidPhoneNumberDoesNotExist() {
        assertThrows(InvalidArgumentException.class, () -> controller.cancelBooking(this.ticket.getNumber(), "123"));
    }
    @Test
    public void cancelBookingInvalidTicketNumberDoesNotExist() throws InvalidArgumentException, ShowNotFoundException {
        assertFalse(controller.cancelBooking(-1, "1234"));
    }
    @Test
    public void cancelBookingInvalidTicketSeatAlreadyNotBooked() {
        this.ticket.setSeatAndRow("B2");
        assertThrows(InvalidArgumentException.class, () -> controller.cancelBooking(this.ticket.getNumber(), "1234"));
    }
    @Test
    public void cancelBookingInvalidTicketShowNotFound() throws InvalidArgumentException, ShowNotFoundException, RowNotFoundException, SeatNotFoundException, InterruptedException {
        Ticket newTicket = controller.bookSeats(1,"12345", "A2").get(0);
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
        assertThrows(InvalidArgumentException.class, () -> controller.cancelBooking(newTicket.getNumber(), "12345"));
    }
}
