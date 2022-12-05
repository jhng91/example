package org.example.controllers;

import org.example.exceptions.*;
import org.example.models.Ticket;
import org.example.services.IShowService;
import org.example.services.ShowServiceImpl;

import java.util.List;

public class ShowController {
    private final IShowService showService;

    public ShowController() {
        this.showService = new ShowServiceImpl();
    }

    public boolean setupShow(int showNumber, int numberOfRows, int numberOfSeats, int cancellationWindow) throws InvalidArgumentException, ShowAlreadyExistsException {
        return showService.setupShow(showNumber, numberOfRows, numberOfSeats, cancellationWindow);
    }
    public String viewShow(int showNumber) throws ShowNotFoundException {
        String output = generateInitialDisplayForSeats(showNumber);
        output += showService.getAllTicketsForShow(showNumber);
        return output;
    }
    public String showAvailability(int showNumber) throws ShowNotFoundException {
        return generateInitialDisplayForSeats(showNumber);
    }
    private String generateInitialDisplayForSeats(int showNumber) throws ShowNotFoundException {
        String seats = showService.getAllSeats(showNumber);
        String output = "Show number: " + showNumber + "\n" + "Seats: \n";
        output += seats;
        return output;
    }
    public List<Ticket> bookSeats(int showNumber, String phoneNumber, String seats) throws InvalidArgumentException, RowNotFoundException, SeatNotFoundException, ShowNotFoundException {
        return showService.bookSeats(showNumber, phoneNumber, seats);
    }
    public boolean cancelBooking(int ticketNumber, String phoneNumber) throws InvalidArgumentException, ShowNotFoundException {
        return showService.cancelBooking(ticketNumber, phoneNumber);
    }
}
