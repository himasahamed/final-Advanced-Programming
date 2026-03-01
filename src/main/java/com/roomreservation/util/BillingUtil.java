package com.roomreservation.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingUtil {

    public static double getRate(String roomType) {
        if (roomType == null) return 0.0;
        return switch (roomType) {
            case "Single" -> 5000.0;
            case "Double" -> 8000.0;
            case "Deluxe" -> 12000.0;
            default -> 0.0;
        };
    }

    public static long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return Math.max(nights, 1); // minimum 1 night
    }

    public static double calculateTotal(String roomType, LocalDate checkIn, LocalDate checkOut) {
        long nights = calculateNights(checkIn, checkOut);
        return nights * getRate(roomType);
    }
}