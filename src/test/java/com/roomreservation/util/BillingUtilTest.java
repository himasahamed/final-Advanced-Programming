package com.roomreservation.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BillingUtilTest {

    @Test
    void testGetRate() {
        assertEquals(5000.0, BillingUtil.getRate("Single"));
        assertEquals(8000.0, BillingUtil.getRate("Double"));
        assertEquals(12000.0, BillingUtil.getRate("Deluxe"));
        assertEquals(0.0, BillingUtil.getRate("Unknown"));
    }

    @Test
    void testCalculateNightsMinimumOne() {
        LocalDate inDate = LocalDate.of(2026, 3, 1);
        LocalDate outDate = LocalDate.of(2026, 3, 1); // same day
        assertEquals(1, BillingUtil.calculateNights(inDate, outDate));
    }

    @Test
    void testCalculateTotal() {
        LocalDate inDate = LocalDate.of(2026, 3, 1);
        LocalDate outDate = LocalDate.of(2026, 3, 4); // 3 nights
        assertEquals(3 * 8000.0, BillingUtil.calculateTotal("Double", inDate, outDate));
    }
}