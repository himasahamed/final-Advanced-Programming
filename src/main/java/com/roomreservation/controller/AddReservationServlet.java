package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddReservationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String guestName = req.getParameter("guest_name");
        String address = req.getParameter("address");
        String contact = req.getParameter("contact");
        String roomType = req.getParameter("room_type");
        String checkIn = req.getParameter("check_in");
        String checkOut = req.getParameter("check_out");

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO reservations (guest_name, address, contact, room_type, check_in, check_out) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, guestName);
            pst.setString(2, address);
            pst.setString(3, contact);
            pst.setString(4, roomType);
            pst.setString(5, checkIn);
            pst.setString(6, checkOut);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                resp.getWriter().println("<h2>✅ Reservation Added Successfully!</h2>");
                resp.getWriter().println("<a href='addReservation.html'>Add Another</a><br>");
                resp.getWriter().println("<a href='index.html'>Go Home</a>");
            } else {
                resp.getWriter().println("<h2>❌ Failed to Add Reservation</h2>");
                resp.getWriter().println("<a href='addReservation.html'>Try Again</a>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
            resp.getWriter().println("<a href='addReservation.html'>Back</a>");
        }
    }
}