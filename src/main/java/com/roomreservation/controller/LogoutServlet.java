package com.roomreservation.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // ✅ Destroy session if exists
        if (req.getSession(false) != null) {
            req.getSession(false).invalidate();
        }

        // ✅ Redirect to Home (logged-out menu)
        resp.sendRedirect("home");
    }
}