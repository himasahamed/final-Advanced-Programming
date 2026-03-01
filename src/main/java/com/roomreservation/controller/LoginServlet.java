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
        resp.setContentType("text/html");

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
                resp.getWriter().println("<h2>✅ Login Success</h2>");
                resp.getWriter().println("<p>Welcome, " + username + "</p>");
                resp.getWriter().println("<a href='index.html'>Go Home</a>");
            } else {
                resp.getWriter().println("<h2>❌ Login Failed</h2>");
                resp.getWriter().println("<p>Invalid username or password</p>");
                resp.getWriter().println("<a href='login.html'>Try Again</a>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
        }
    }
}