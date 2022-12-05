package org.example.services;

import org.example.daos.ShowDao;
import org.example.daos.TicketDao;
import org.example.exceptions.*;
import org.example.models.Seat;
import org.example.models.Show;
import org.example.models.Ticket;
import org.example.utils.ShowUtils;
import org.example.utils.validators.CancellationWindowValidator;
import org.example.utils.validators.RowValidator;
import org.example.utils.validators.SeatValidator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShowServiceImpl implements IShowService {
    private final ShowDao showDao;
    private final TicketDao ticketDao;

    public ShowServiceImpl() {
        showDao = new ShowDao();
        ticketDao = new TicketDao();
    }
    @Override
    public boolean setupShow(int showNumber, int numberOfRows, int numberOfSeats, int cancellationWindow) throws InvalidArgumentException, ShowAlreadyExistsException {
        validateAll(numberOfRows, numberOfSeats, cancellationWindow);
        if(showDao.get(showNumber).isPresent())
            throw new ShowAlreadyExistsException();
        showDao.save(new Show(showNumber, cancellationWindow, new ShowUtils().setupSeats(numberOfRows, numberOfSeats)));
        return true;
    }

    @Override
    public String getAllSeats(int showNumber) throws ShowNotFoundException {
        Show show = showDao.get(showNumber).orElseThrow(ShowNotFoundException::new);
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Seat[]> entry : show.getSeats().entrySet()) {
            String key = entry.getKey();
            Seat[] value = entry.getValue();
            output.append(key).append(": ");
            for(int i = 1; i <= value.length; i++) {
                if(value[i - 1] == null)
                    output.append(i).append(" ");
                else
                    output.append("X ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    @Override
    public List<Ticket> bookSeats(int showNumber, String phoneNumber, String seatsString) throws InvalidArgumentException, SeatNotFoundException, RowNotFoundException, ShowNotFoundException {
        Optional<Show> optionalShow = showDao.get(showNumber);
        if(optionalShow.isEmpty())
            throw new ShowNotFoundException();
        String[] seatArray = seatsString.split(",");
        Show show = optionalShow.get();

        // check availability
        for(String rowAndSeat : seatArray) {
            String row = rowAndSeat.substring(0, 1);
            int seatNumber = Integer.parseInt(rowAndSeat.substring(1)) - 1;
            // validate row
            if(!new RowValidator().validateRowInShow(row, show.getSeats().keySet()))
                throw new RowNotFoundException();
            Seat[] showSeats = show.getSeats().get(row);
            // validate seat
            if(!new SeatValidator().validateValidSeatIndex(showSeats, seatNumber))
                throw new SeatNotFoundException();
            if(showSeats[seatNumber] != null)
                throw new InvalidArgumentException("Selected seat is not available.");
        }

        Optional<List<Ticket>> optionalTicketList = ticketDao.get(phoneNumber);
        List<Ticket> ticketList;
        if(optionalTicketList.isPresent()) {
            throw new InvalidArgumentException("Booking already made on this number!");
        }
        ticketList = new ArrayList<>();
        ticketDao.save(phoneNumber, ticketList);
        ArrayList<Ticket> newTicketsList = new ArrayList<>();
        // book seats
        for(String rowAndSeat : seatArray) {
            String row = rowAndSeat.substring(0, 1);
            int seatNumber = Integer.parseInt(rowAndSeat.substring(1)) - 1;
            Seat[] showSeats = show.getSeats().get(row);
            showSeats[seatNumber] = new Seat(new Timestamp(System.currentTimeMillis()).getTime(), phoneNumber);
            Ticket ticket = new Ticket(showNumber, rowAndSeat);
            ticketList.add(ticket);
            newTicketsList.add(ticket);
        }
        return newTicketsList;
    }

    @Override
    public boolean cancelBooking(int ticketNumber, String phoneNumber) throws InvalidArgumentException, ShowNotFoundException {
        // check if ticket exists
        List<Ticket> ticketList = ticketDao.get(phoneNumber).orElseThrow(() ->
                new InvalidArgumentException("Phone number: " + phoneNumber + " has not made a booking!"));
        // check if seat is booked and time is ok
        Show show = null;
        String row = null;
        int seatNumber = -1;
        boolean found = false;
        for(Ticket ticket : ticketList) {
            if(ticket.getNumber() != ticketNumber)
                continue;
            show = showDao.get(ticket.getShowNumber()).orElseThrow(ShowNotFoundException::new);
            row = ticket.getSeatAndRow().substring(0, 1);
            Seat[] seats = show.getSeats().get(row);
            seatNumber = Integer.parseInt(ticket.getSeatAndRow().substring(1)) - 1;
            if(seats[seatNumber] == null)
                throw new InvalidArgumentException("Seat " + ticket.getSeatAndRow() + " is already empty!");
            if((new Timestamp(System.currentTimeMillis()).getTime() - seats[seatNumber].getTimestamp()) >
                    (show.getCancellationWindow() * 60000L))
                throw new InvalidArgumentException("Cancellation window is over.");
            found = true;
        }
        // assign seat to null
        if(found) {
            show.getSeats().get(row)[seatNumber] = null;
            ticketDao.delete(phoneNumber, ticketNumber);
            return true;
        }
        return false;
    }

    @Override
    public String getAllTicketsForShow(int showNumber) throws ShowNotFoundException {
        showDao.get(showNumber).orElseThrow(ShowNotFoundException::new);
        StringBuilder output = new StringBuilder();
        Map<String, List<Ticket>> ticketsMap = ticketDao.getAll();
        ticketsMap.forEach((k, v) -> {
            output.append("Phone number: ").append(k).append(" Show number: ").append(showNumber).append(" Tickets: ");
            v.forEach(ticket -> {
                output.append(ticket.getSeatAndRow()).append(" ");
            });
            output.append("\n");
        });
        return output.toString();
    }

    protected void validateAll(int numberOfRows, int numberOfSeats, int cancellationWindow) throws InvalidArgumentException {
        if(!new RowValidator().validateNumberOfRows(numberOfRows))
            throw new InvalidArgumentException("Number of rows must be between 1 and 26.");
        else if(!new SeatValidator().validateNumberOfSeats(numberOfSeats))
            throw new InvalidArgumentException("Number of seats per row must be between 1 and 10.");
        else if(!new CancellationWindowValidator().validate(cancellationWindow))
            throw new InvalidArgumentException("Cancellation window must be a positive number.");
    }
}
