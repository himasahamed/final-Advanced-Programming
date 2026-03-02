package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

public class AddReservationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // ✅ Protect (only logged in users)
        if (req.getSession(false) == null || req.getSession(false).getAttribute("loggedInUser") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        resp.setContentType("text/html; charset=UTF-8");

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

            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, guestName);
            pst.setString(2, address);
            pst.setString(3, contact);
            pst.setString(4, roomType);
            pst.setString(5, checkIn);
            pst.setString(6, checkOut);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                int reservationNo = -1;
                ResultSet keys = pst.getGeneratedKeys();
                if (keys.next()) {
                    reservationNo = keys.getInt(1);
                }

                resp.getWriter().println(successPage(reservationNo));
            } else {
                resp.getWriter().println(errorPage("Failed to add reservation.", "addReservation.html"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println(errorPage("Error: " + e.getMessage(), "addReservation.html"));
        }
    }

    private String successPage(int reservationNo) {
        String viewLink = "viewReservation?reservation_no=" + reservationNo;
        String billLink = "bill?reservation_no=" + reservationNo;

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <title>Reservation Added</title>

          <style>
            :root{
              --bg1:#0f172a; --bg2:#111827;
              --card: rgba(255,255,255,.07);
              --border: rgba(255,255,255,.14);
              --text: rgba(255,255,255,.92);
              --muted: rgba(255,255,255,.68);
              --accent:#3b82f6; --accent2:#22c55e;
              --danger:#ef4444;
              --shadow: 0 18px 40px rgba(0,0,0,.45);
              --radius: 18px;
            }
            *{box-sizing:border-box;}
            body{
              margin:0; min-height:100vh; padding:24px;
              font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
              color:var(--text);
              display:flex; align-items:center; justify-content:center;
              background:
                radial-gradient(900px 500px at 15%% 20%%, rgba(59,130,246,.35), transparent 55%%),
                radial-gradient(700px 420px at 85%% 25%%, rgba(34,197,94,.25), transparent 60%%),
                radial-gradient(900px 600px at 50%% 90%%, rgba(168,85,247,.18), transparent 60%%),
                linear-gradient(135deg, var(--bg1), var(--bg2));
            }
            .wrap{ width:min(820px, 100%%); display:grid; gap:16px; }
            .card{
              border:1px solid var(--border);
              background: var(--card);
              backdrop-filter: blur(10px);
              border-radius: var(--radius);
              box-shadow: var(--shadow);
              padding: 18px;
            }
            .top{
              display:flex; align-items:center; justify-content:space-between; gap:12px;
              padding:14px 16px;
              border:1px solid var(--border);
              background: rgba(255,255,255,.06);
              backdrop-filter: blur(10px);
              border-radius: var(--radius);
              box-shadow: var(--shadow);
            }
            .brand{ display:flex; align-items:center; gap:10px; font-weight:800; }
            .logo{
              width:38px;height:38px;border-radius:12px;
              background: linear-gradient(135deg, rgba(59,130,246,.95), rgba(34,197,94,.85));
              box-shadow: 0 10px 22px rgba(59,130,246,.25);
            }
            .subtitle{ font-size:13px; color:var(--muted); margin-top:2px; }

            h2{ margin:0 0 8px 0; font-size:22px; }
            .pill{
              display:inline-block;
              margin-top:10px;
              padding:10px 12px;
              border-radius: 16px;
              border:1px solid rgba(34,197,94,.35);
              background: rgba(34,197,94,.12);
              color: rgba(187,247,208,.95);
              font-weight:800;
            }

            .actions{
              margin-top:14px;
              display:flex;
              gap:10px;
              flex-wrap:wrap;
              justify-content:flex-end;
            }
            a.btn{
              text-decoration:none;
              display:inline-flex;
              align-items:center;
              justify-content:center;
              padding: 12px 14px;
              border-radius: 14px;
              font-weight: 800;
              font-size:14px;
              border:1px solid rgba(255,255,255,.14);
              background: rgba(255,255,255,.06);
              color: var(--text);
              transition: .2s ease;
              min-width: 160px;
            }
            a.btn:hover{
              transform: translateY(-1px);
              border-color: rgba(59,130,246,.45);
              background: rgba(59,130,246,.12);
            }
            a.btn.primary{
              border:none;
              color:white;
              background: linear-gradient(135deg, rgba(59,130,246,.95), rgba(34,197,94,.85));
              box-shadow: 0 14px 28px rgba(59,130,246,.22);
            }
            a.btn.danger{
              border:none;
              color:white;
              background: linear-gradient(135deg, rgba(239,68,68,.95), rgba(244,63,94,.85));
            }

            .footer{ text-align:center; color: rgba(255,255,255,.55); font-size:12px; margin-top:6px; }

            @media (max-width: 720px){
              a.btn{ width:100%%; }
              .actions{ justify-content:stretch; }
              .top{ flex-direction:column; align-items:flex-start; }
            }
          </style>
        </head>
        <body>
          <div class="wrap">

            <div class="top">
              <div class="brand">
                <div class="logo"></div>
                <div>
                  <div>Room Reservation System</div>
                  <div class="subtitle">Reservation created successfully</div>
                </div>
              </div>
              <div class="subtitle">Reservation No: <b>%d</b></div>
            </div>

            <div class="card">
              <h2>✅ Reservation Added Successfully!</h2>
              <div class="pill">Your Reservation Number is: %d</div>

              <div class="actions">
                <a class="btn" href="addReservation.html">Add Another</a>
                <a class="btn primary" href="%s">View Reservation</a>
                <a class="btn primary" href="%s">Generate Bill</a>
                <a class="btn" href="home">Go Home</a>
              </div>
            </div>

            <div class="footer">Tip: Use Reservation No to View and Generate Bill.</div>

          </div>
        </body>
        </html>
        """.formatted(reservationNo, reservationNo, viewLink, billLink);
    }

    private String errorPage(String message, String backLink) {
        return """
            <!DOCTYPE html>
            <html><head><meta charset="UTF-8"><title>Error</title></head>
            <body style="font-family:Arial; padding:20px;">
              <h2>❌ %s</h2>
              <a href="%s">Back</a>
            </body></html>
            """.formatted(message, backLink);
    }
}