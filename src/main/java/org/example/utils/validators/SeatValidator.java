package org.example.utils.validators;

import org.example.models.Seat;

public class SeatValidator {
    public boolean validateNumberOfSeats(int numberOfSeats) {
        return numberOfSeats > 0 && numberOfSeats <= 10;
    }
    public boolean validateValidSeatIndex(Seat[] seats, int seatNumber) {
        return seatNumber >= 0 && seatNumber < seats.length;
    }
}

