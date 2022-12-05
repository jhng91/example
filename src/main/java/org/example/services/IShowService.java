package org.example.services;

import org.example.exceptions.*;
import org.example.models.Ticket;

import java.util.List;

public interface IShowService {
    boolean setupShow(int showNumber, int numberOfRows, int NumberOfSeats, int cancellationWindow) throws InvalidArgumentException, ShowAlreadyExistsException;
    String getAllSeats(int showNumber) throws ShowNotFoundException;
    List<Ticket> bookSeats(int showNumber, String phoneNumber, String seats) throws InvalidArgumentException, SeatNotFoundException, RowNotFoundException, ShowNotFoundException;
    boolean cancelBooking(int ticketNumber, String phoneNumber) throws InvalidArgumentException, ShowNotFoundException;
    String getAllTicketsForShow(int showNumber) throws ShowNotFoundException;

}
