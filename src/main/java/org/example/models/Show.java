package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Show {
    private int number;
    private int cancellationWindow;
    private Map<String, Seat[]> seats;


    public Show(int number, int cancellationWindow, Map<String, Seat[]> seats) {
         this.number = number;
         this.cancellationWindow = cancellationWindow;
         this.seats = seats;
    }
}
