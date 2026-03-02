package com.roomreservation.controller;

import com.roomreservation.util.DBConnection;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // ✅ Only logged-in users can view reports
        if (req.getSession(false) == null || req.getSession(false).getAttribute("loggedInUser") == null) {
            resp.sendRedirect("login.html");
            return;
        }

        resp.setContentType("text/html; charset=UTF-8");

        StringBuilder rows = new StringBuilder();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT reservation_no, guest_name, contact, room_type, check_in, check_out, created_at " +
                    "FROM reservations ORDER BY reservation_no DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                rows.append("<tr>")
                        .append("<td>").append(rs.getInt("reservation_no")).append("</td>")
                        .append("<td>").append(escape(rs.getString("guest_name"))).append("</td>")
                        .append("<td>").append(escape(rs.getString("contact"))).append("</td>")
                        .append("<td>").append(escape(rs.getString("room_type"))).append("</td>")
                        .append("<td>").append(rs.getString("check_in")).append("</td>")
                        .append("<td>").append(rs.getString("check_out")).append("</td>")
                        .append("<td>")
                        .append("<a class='link' href='viewReservation?reservation_no=")
                        .append(rs.getInt("reservation_no"))
                        .append("'>View</a>")
                        .append(" | ")
                        .append("<a class='link' href='bill?reservation_no=")
                        .append(rs.getInt("reservation_no"))
                        .append("'>Bill</a>")
                        .append("</td>")
                        .append("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("<h2>❌ Error: " + e.getMessage() + "</h2>");
            resp.getWriter().println("<a href='reports.html'>Back</a>");
            return;
        }

        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Reservations Report</title>

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
                .wrap{ width:min(1100px, 100%%); display:grid; gap:16px; }

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
                .subtitle{ font-size:13px; color:var(--muted); margin-top:2px; }

                .nav a{
                  color:var(--text);
                  text-decoration:none;
                  padding:10px 12px;
                  border-radius:12px;
                  border:1px solid transparent;
                  background: rgba(255,255,255,.05);
                  transition: .2s ease;
                  font-size:14px;
                  white-space:nowrap;
                }
                .nav a:hover{
                  border-color: rgba(59,130,246,.45);
                  background: rgba(59,130,246,.12);
                  transform: translateY(-1px);
                }

                .card{
                  border:1px solid var(--border);
                  background: var(--card);
                  backdrop-filter: blur(10px);
                  border-radius: var(--radius);
                  box-shadow: var(--shadow);
                  padding: 18px;
                }

                h2{ margin:0 0 6px 0; font-size:22px; }
                .hint{ margin:0 0 12px 0; color:var(--muted); font-size:14px; }

                .tableWrap{
                  overflow:auto;
                  border-radius: 16px;
                  border:1px solid rgba(255,255,255,.12);
                  background: rgba(255,255,255,.05);
                }

                table{
                  width:100%%;
                  border-collapse: collapse;
                  min-width: 880px;
                }
                th, td{
                  padding: 12px;
                  font-size: 14px;
                  border-bottom:1px solid rgba(255,255,255,.10);
                  text-align:left;
                  vertical-align:top;
                }
                th{ color: var(--muted); font-weight:800; }
                tr:hover td{ background: rgba(59,130,246,.08); }

                .link{
                  color: rgba(147,197,253,.95);
                  text-decoration:none;
                  font-weight:800;
                }
                .link:hover{ text-decoration:underline; }

                @media (max-width: 900px){
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
                      <div class="subtitle">Reservations Report</div>
                    </div>
                  </div>

                  <div class="nav">
                    <a href="home">Home</a>
                    <a href="addReservation.html">Add</a>
                    <a href="viewReservation.html">View</a>
                    <a href="bill.html">Bill</a>
                    <a href="help.html">Help</a>
                  </div>
                </div>

                <div class="card">
                  <h2>All Reservations</h2>
                  <p class="hint">Click View or Bill to open each reservation.</p>

                  <div class="tableWrap">
                    <table>
                      <thead>
                        <tr>
                          <th>No</th>
                          <th>Guest Name</th>
                          <th>Contact</th>
                          <th>Room Type</th>
                          <th>Check-in</th>
                          <th>Check-out</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        %s
                      </tbody>
                    </table>
                  </div>
                </div>

              </div>
            </body>
            </html>
        """.formatted(rows.length() == 0
                ? "<tr><td colspan='7'>No reservations found.</td></tr>"
                : rows.toString());

        resp.getWriter().println(html);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}