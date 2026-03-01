package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillServlet extends HttpServlet {

    private double getRate(String roomType) {
        // Simple rates (you can change later)
        return switch (roomType) {
            case "Single" -> 5000.0;
            case "Double" -> 8000.0;
            case "Deluxe" -> 12000.0;
            default -> 0.0;
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String resNoStr = req.getParameter("reservation_no");
        if (resNoStr == null || resNoStr.isEmpty()) {
            resp.getWriter().println("<h2>❌ Reservation No is required</h2>");
            resp.getWriter().println("<a href='bill.html'>Back</a>");
            return;
        }

        try {
            int reservationNo = Integer.parseInt(resNoStr);

            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM reservations WHERE reservation_no = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, reservationNo);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                resp.getWriter().println("<h2>❌ No reservation found for No: " + reservationNo + "</h2>");
                resp.getWriter().println("<a href='bill.html'>Try Again</a>");
                return;
            }

            String guestName = rs.getString("guest_name");
            String contact = rs.getString("contact");
            String roomType = rs.getString("room_type");
            LocalDate checkIn = rs.getDate("check_in").toLocalDate();
            LocalDate checkOut = rs.getDate("check_out").toLocalDate();

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights <= 0) nights = 1; // safety

            double rate = getRate(roomType);
            double total = nights * rate;

            // Printable Bill HTML
            resp.getWriter().println("""
                    <html><head><title>Bill</title></head><body>
                    <h2>Room Reservation Bill</h2>
                    <hr>
                    <p><b>Reservation No:</b> %d</p>
                    <p><b>Guest Name:</b> %s</p>
                    <p><b>Contact:</b> %s</p>
                    <p><b>Room Type:</b> %s</p>
                    <p><b>Check-in:</b> %s</p>
                    <p><b>Check-out:</b> %s</p>
                    <p><b>Nights:</b> %d</p>
                    <p><b>Rate per Night:</b> %.2f</p>
                    <h3><b>Total Amount:</b> %.2f</h3>
                    <hr>
                    <button onclick="window.print()">Print Bill</button>
                    <br><br>
                    <a href="bill.html">Generate Another</a><br>
                    <a href="index.html">Go Home</a>
                    </body></html>
                    """.formatted(reservationNo, guestName, contact, roomType, checkIn, checkOut, nights, rate, total));

        } catch (NumberFormatException e) {
            resp.getWriter().println("<h2>❌ Invalid Reservation No</h2>");
            resp.getWriter().println("<a href='bill.html'>Back</a>");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
        }
    }
}