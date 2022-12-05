package org.example;

import org.example.controllers.ShowController;
import org.example.exceptions.*;
import org.example.models.Ticket;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestBookSeats {
    ShowController controller;
    @Before
    public void init() throws InvalidArgumentException, ShowAlreadyExistsException {
        controller = new ShowController();
        controller.setupShow(1, 10, 10, 10);
    }

    @Test
    public void testBookSeatsValid() throws InvalidArgumentException, RowNotFoundException, SeatNotFoundException, ShowNotFoundException {
        List<Ticket> result = controller.bookSeats(1, "1234", "A1,J10");
        assertEquals(2, result.size());
    }
    @Test
    public void testBookSeatsShowNotFound() {
        assertThrows(ShowNotFoundException.class, () -> controller.bookSeats(2, "1234", "A1,B2"));
    }
    @Test
    public void testBookSeatsRowNotFound() {
        assertThrows(RowNotFoundException.class, () -> controller.bookSeats(1, "1234", "Z1"));
    }
    @Test
    public void testBookSeatsSeatsNotFound() {
        assertThrows(SeatNotFoundException.class, () -> controller.bookSeats(1, "1234", "A11"));
    }
    @Test
    public void testBookSeatsSeatAlreadyBooked() throws InvalidArgumentException, ShowNotFoundException, RowNotFoundException, SeatNotFoundException {
        controller.bookSeats(1, "12345", "A1");
        assertThrows(InvalidArgumentException.class, () -> controller.bookSeats(1, "1234", "A1"));
    }
    @Test
    public void testBookSeatsPhoneNumberAlreadyBooked() throws InvalidArgumentException, ShowNotFoundException, RowNotFoundException, SeatNotFoundException {
        controller.bookSeats(1, "1234", "A1");
        assertThrows(InvalidArgumentException.class, () -> controller.bookSeats(1, "1234", "A2"));
    }
    @Test
    public void testBookSeatsPhoneNumberAlreadyBookedDifferentShows() throws InvalidArgumentException, ShowAlreadyExistsException, ShowNotFoundException, RowNotFoundException, SeatNotFoundException {
        controller.setupShow(2, 10, 10, 1);
        List<Ticket> ticketList = controller.bookSeats(1, "1234", "A1");
        ticketList.addAll(controller.bookSeats(2, "1234", "A1"));
        assertEquals(2, ticketList.size());
    }
}
