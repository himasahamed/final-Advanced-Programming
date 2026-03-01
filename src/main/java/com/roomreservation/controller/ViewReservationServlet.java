package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String resNoStr = req.getParameter("reservation_no");

        if (resNoStr == null || resNoStr.isEmpty()) {
            resp.getWriter().println("<h2>❌ Reservation No is required</h2>");
            resp.getWriter().println("<a href='viewReservation.html'>Back</a>");
            return;
        }

        try {
            int reservationNo = Integer.parseInt(resNoStr);

            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM reservations WHERE reservation_no = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, reservationNo);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                resp.getWriter().println("<h2>✅ Reservation Details</h2>");
                resp.getWriter().println("<p><b>Reservation No:</b> " + rs.getInt("reservation_no") + "</p>");
                resp.getWriter().println("<p><b>Guest Name:</b> " + rs.getString("guest_name") + "</p>");
                resp.getWriter().println("<p><b>Address:</b> " + rs.getString("address") + "</p>");
                resp.getWriter().println("<p><b>Contact:</b> " + rs.getString("contact") + "</p>");
                resp.getWriter().println("<p><b>Room Type:</b> " + rs.getString("room_type") + "</p>");
                resp.getWriter().println("<p><b>Check-in:</b> " + rs.getString("check_in") + "</p>");
                resp.getWriter().println("<p><b>Check-out:</b> " + rs.getString("check_out") + "</p>");

                resp.getWriter().println("<br><a href='viewReservation.html'>Search Another</a>");
                resp.getWriter().println("<br><a href='index.html'>Go Home</a>");

            } else {
                resp.getWriter().println("<h2>❌ No reservation found for No: " + reservationNo + "</h2>");
                resp.getWriter().println("<a href='viewReservation.html'>Try Again</a>");
            }

        } catch (NumberFormatException e) {
            resp.getWriter().println("<h2>❌ Invalid Reservation No</h2>");
            resp.getWriter().println("<a href='viewReservation.html'>Back</a>");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
        }
    }
}