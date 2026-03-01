package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // ✅ Create session (login success)
                req.getSession(true).setAttribute("loggedInUser", username);

                // ✅ Redirect to HomeServlet
                resp.sendRedirect("home");
                return;

            } else {
                // ❌ Login failed → redirect back to login page with error flag
                resp.sendRedirect("login.html?error=1");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
            resp.getWriter().println("<a href='login.html'>Back</a>");
        }
    }
}