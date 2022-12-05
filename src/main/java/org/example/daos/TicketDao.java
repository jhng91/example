package org.example.daos;

import org.example.models.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TicketDao {
    private final Map<String, List<Ticket>> tickets;

    public TicketDao() {
        this.tickets = new HashMap<>();
    }

    public Optional<List<Ticket>> get(String number) { return Optional.ofNullable(this.tickets.get(number)); }
    public void save(String phoneNumber, List<Ticket> ticketList) { this.tickets.put(phoneNumber, ticketList); }
    public void delete(String phoneNumber, Integer ticketNumber) {
        this.tickets.get(phoneNumber).removeIf(ticket -> ticketNumber.equals(ticket.getNumber()));
    }
    public Map<String, List<Ticket>> getAll() { return this.tickets; }
}
