package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    private static int count = 0;
    private int showNumber;
    private String seatAndRow;
    private int number;

    public Ticket(int showNumber, String seatAndRow) {
        count++;
        this.number = count;
        this.seatAndRow = seatAndRow;
        this.showNumber = showNumber;
    }
}
