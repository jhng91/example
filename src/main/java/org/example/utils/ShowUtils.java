package org.example.utils;

import org.example.models.Seat;

import java.util.HashMap;
import java.util.Map;

public class ShowUtils {
    public Map<String, Seat[]> setupSeats(int numberOfRows, int numberOfSeatsPerRow) {
        Map<String, Seat[]> seats = new HashMap<>();
        char row = 'A';
        for (int i = 1; i <= numberOfRows; i++) {
            seats.put(Character.toString(row), new Seat[numberOfSeatsPerRow]);
            row++;
        }
        return seats;
    }
}
