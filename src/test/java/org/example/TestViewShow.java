package org.example;
import org.example.controllers.ShowController;
import org.example.exceptions.*;
import org.example.services.IShowService;
import org.example.services.ShowServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestViewShow {
    IShowService service;
    ShowController controller;
    @Before
    public void init() throws InvalidArgumentException, ShowAlreadyExistsException {
        service = new ShowServiceImpl();
        service.setupShow(1, 10, 10, 10);
        controller = new ShowController();
        controller.setupShow(2, 10, 10, 10);
    }

    @Test
    public void testGetAllSeats() throws ShowNotFoundException, InvalidArgumentException, RowNotFoundException, SeatNotFoundException {
        service.bookSeats(1, "1234", "A1");
        assertNotEquals(0, service.getAllSeats(1).length());
    }
    @Test
    public void testGetAllSeatsShowNotFound() throws ShowNotFoundException {
        assertThrows(ShowNotFoundException.class, () -> service.getAllSeats(2));
    }

    @Test
    public void testGetAllTicketsForShowEmpty() throws ShowNotFoundException {
        assertEquals(0, service.getAllTicketsForShow(1).length());
    }
    @Test
    public void testGetAllTicketsForShow() throws ShowNotFoundException, InvalidArgumentException, RowNotFoundException, SeatNotFoundException {
        service.bookSeats(1, "1234", "A1");
        assertNotEquals(0, service.getAllTicketsForShow(1).length());
    }
    @Test
    public void testGetAllTicketsForShowNotFound() {
        assertThrows(ShowNotFoundException.class, () -> service.getAllTicketsForShow(2));
    }
    @Test
    public void testViewShow() throws ShowNotFoundException {
        assertTrue(controller.viewShow(2).length() > 0);
    }
    @Test
    public void testViewAvailability() throws ShowNotFoundException {
        assertTrue(controller.showAvailability(2).length() > 0);
    }
}
