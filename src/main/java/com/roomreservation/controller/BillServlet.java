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
        return switch (roomType) {
            case "Single" -> 5000.0;
            case "Double" -> 8000.0;
            case "Deluxe" -> 12000.0;
            default -> 0.0;
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        String resNoStr = req.getParameter("reservation_no");
        if (resNoStr == null || resNoStr.isEmpty()) {
            resp.getWriter().println(errorPage("Reservation No is required.", "bill.html"));
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
                resp.getWriter().println(errorPage("No reservation found for No: " + reservationNo, "bill.html"));
                return;
            }

            String guestName = rs.getString("guest_name");
            String contact = rs.getString("contact");
            String address = rs.getString("address");
            String roomType = rs.getString("room_type");
            LocalDate checkIn = rs.getDate("check_in").toLocalDate();
            LocalDate checkOut = rs.getDate("check_out").toLocalDate();

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights <= 0) nights = 1;

            double rate = getRate(roomType);
            double total = nights * rate;

            String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Bill - Reservation %d</title>
                  <style>
                    :root{
                      --bg1:#0f172a; --bg2:#111827;
                      --card: rgba(255,255,255,.07);
                      --border: rgba(255,255,255,.14);
                      --text: rgba(255,255,255,.92);
                      --muted: rgba(255,255,255,.68);
                      --accent:#3b82f6; --accent2:#22c55e;
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
                    .wrap{ width:min(980px, 100%%); display:grid; gap:16px; }
                    .topbar{
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
                    .subtitle{ font-size:13px; color:var(--muted); font-weight:500; margin-top:2px; }
                    .nav a{
                      color:var(--text); text-decoration:none; padding:10px 12px;
                      border-radius:12px; border:1px solid transparent;
                      background: rgba(255,255,255,.05); transition: .2s ease;
                      font-size:14px; white-space:nowrap;
                    }
                    .nav a:hover{
                      border-color: rgba(59,130,246,.45);
                      background: rgba(59,130,246,.12);
                      transform: translateY(-1px);
                    }

                    .invoice{
                      border:1px solid var(--border);
                      background: var(--card);
                      backdrop-filter: blur(10px);
                      border-radius: var(--radius);
                      box-shadow: var(--shadow);
                      padding: 18px;
                    }

                    .header{
                      display:flex;
                      justify-content:space-between;
                      gap:14px;
                      align-items:flex-start;
                      flex-wrap:wrap;
                      margin-bottom:12px;
                    }
                    h2{ margin:0 0 6px 0; font-size:22px; }
                    .small{ margin:0; color:var(--muted); font-size:13px; line-height:1.4; }

                    .grid{
                      display:grid;
                      grid-template-columns: 1fr 1fr;
                      gap:12px;
                      margin-top:10px;
                    }
                    .box{
                      border:1px solid rgba(255,255,255,.12);
                      background: rgba(255,255,255,.05);
                      border-radius: 16px;
                      padding: 12px;
                    }
                    .box b{ display:block; font-size:12px; color:var(--muted); margin-bottom:6px; }
                    .box span{ font-size:14px; }

                    table{
                      width:100%%;
                      border-collapse: collapse;
                      margin-top:12px;
                      overflow:hidden;
                      border-radius: 16px;
                      border:1px solid rgba(255,255,255,.12);
                      background: rgba(255,255,255,.05);
                    }
                    th, td{
                      padding: 12px;
                      font-size: 14px;
                      border-bottom:1px solid rgba(255,255,255,.10);
                      text-align:left;
                    }
                    th{ color: var(--muted); font-weight:700; }
                    tr:last-child td{ border-bottom:none; }

                    .total{
                      display:flex;
                      justify-content:flex-end;
                      margin-top:12px;
                    }
                    .total .pill{
                      padding:12px 14px;
                      border-radius: 16px;
                      border:1px solid rgba(34,197,94,.35);
                      background: rgba(34,197,94,.12);
                      color: rgba(187,247,208,.95);
                      font-weight:900;
                      font-size: 16px;
                    }

                    .actions{
                      display:flex;
                      gap:10px;
                      justify-content:flex-end;
                      flex-wrap:wrap;
                      margin-top:14px;
                    }
                    .btn{
                      border:none; cursor:pointer;
                      padding:12px 14px;
                      border-radius: 14px;
                      font-weight: 800;
                      transition:.2s ease;
                      font-size:14px;
                    }
                    .btn-primary{
                      color:white;
                      background: linear-gradient(135deg, rgba(59,130,246,.95), rgba(34,197,94,.85));
                      box-shadow: 0 14px 28px rgba(59,130,246,.22);
                    }
                    .btn-primary:hover{ transform: translateY(-1px); filter: brightness(1.02); }
                    .btn-secondary{
                      color: var(--text);
                      background: rgba(255,255,255,.08);
                      border:1px solid rgba(255,255,255,.14);
                    }
                    .btn-secondary:hover{ transform: translateY(-1px); border-color: rgba(255,255,255,.22); }

                    /* Print-friendly */
                    @media print{
                      body{ background:white !important; color:black !important; padding:0 !important; }
                      .topbar, .actions, .nav { display:none !important; }
                      .invoice{ box-shadow:none !important; border:1px solid #ddd !important; background:white !important; }
                      table, .box{ background:white !important; }
                      th{ color:#444 !important; }
                      .total .pill{ border:1px solid #999 !important; background:#f4f4f4 !important; color:#000 !important; }
                    }
                    @media (max-width: 820px){
                      .grid{ grid-template-columns: 1fr; }
                      .topbar{ flex-direction:column; align-items:flex-start; }
                      .nav{ width:100%%; display:flex; gap:10px; flex-wrap:wrap; }
                    }
                  </style>
                </head>
                <body>
                  <div class="wrap">
                    <div class="topbar">
                      <div class="brand">
                        <div class="logo"></div>
                        <div>
                          <div>Room Reservation System</div>
                          <div class="subtitle">Bill / Invoice</div>
                        </div>
                      </div>
                      <div class="nav">
                        <a href="index.html">Home</a>
                        <a href="viewReservation.html">View</a>
                        <a href="bill.html">Generate Another</a>
                      </div>
                    </div>

                    <div class="invoice">
                      <div class="header">
                        <div>
                          <h2>Invoice</h2>
                          <p class="small">Reservation No: <b>%d</b></p>
                          <p class="small">Generated on: <b>%s</b></p>
                        </div>
                        <div>
                          <p class="small"><b>Hotel:</b> Room Reservation System</p>
                          <p class="small"><b>Contact:</b> +94 XXXXXXXX</p>
                        </div>
                      </div>

                      <div class="grid">
                        <div class="box">
                          <b>Guest</b>
                          <span>%s</span><br>
                          <span class="small">%s</span><br>
                          <span class="small">%s</span>
                        </div>
                        <div class="box">
                          <b>Stay Details</b>
                          <span><b>Room:</b> %s</span><br>
                          <span class="small"><b>Check-in:</b> %s</span><br>
                          <span class="small"><b>Check-out:</b> %s</span>
                        </div>
                      </div>

                      <table>
                        <thead>
                          <tr>
                            <th>Description</th>
                            <th>Nights</th>
                            <th>Rate / Night</th>
                            <th>Amount</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>%s Room Booking</td>
                            <td>%d</td>
                            <td>%.2f</td>
                            <td>%.2f</td>
                          </tr>
                        </tbody>
                      </table>

                      <div class="total">
                        <div class="pill">Total Amount: %.2f</div>
                      </div>

                      <div class="actions">
                        <button class="btn btn-primary" onclick="window.print()">Print / Save as PDF</button>
                        <a class="btn btn-secondary" href="bill.html" style="text-decoration:none; display:inline-block;">Back</a>
                        <a class="btn btn-secondary" href="index.html" style="text-decoration:none; display:inline-block;">Home</a>
                      </div>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                    reservationNo,
                    reservationNo,
                    LocalDate.now(),
                    escape(guestName),
                    escape(contact),
                    escape(address == null ? "" : address),
                    escape(roomType),
                    checkIn,
                    checkOut,
                    escape(roomType),
                    nights,
                    rate,
                    total,
                    total
            );

            resp.getWriter().println(html);

        } catch (NumberFormatException e) {
            resp.getWriter().println(errorPage("Invalid Reservation No.", "bill.html"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println(errorPage("Error: " + e.getMessage(), "bill.html"));
        }
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

    // Very small HTML escape to avoid breaking HTML output
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}