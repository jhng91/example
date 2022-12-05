package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Seat {
    @Getter
    private long timestamp;
    private String phoneNumber;
}
