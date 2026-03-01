package com.roomreservation.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        boolean loggedIn = req.getSession(false) != null &&
                req.getSession(false).getAttribute("loggedInUser") != null;

        String username = loggedIn ? String.valueOf(req.getSession(false).getAttribute("loggedInUser")) : "";

        String cards = loggedIn
                ? """
                <div class="grid">
                  <div class="card">
                    <h3>Add New Reservation</h3>
                    <p>Create a reservation with guest details, room type, and dates.</p>
                    <div class="btnrow"><a class="btn primary" href="addReservation.html">Open</a></div>
                  </div>

                  <div class="card">
                    <h3>View Reservations</h3>
                    <p>Search a reservation using the Reservation Number.</p>
                    <div class="btnrow"><a class="btn primary" href="viewReservation.html">Open</a></div>
                  </div>

                  <div class="card">
                    <h3>Generate Bill</h3>
                    <p>Calculate nights and generate a printable invoice.</p>
                    <div class="btnrow"><a class="btn primary" href="bill.html">Open</a></div>
                  </div>

                  <div class="card">
                    <h3>Help</h3>
                    <p>Quick guide for using each feature in the system.</p>
                    <div class="btnrow"><a class="btn" href="help.html">Open</a></div>
                  </div>

                  <div class="card">
                    <h3>Logout</h3>
                    <p>End your session and return to Home.</p>
                    <div class="btnrow"><a class="btn danger" href="logout">Logout</a></div>
                  </div>
                </div>
                """
                : """
                <div class="grid grid2">
                  <div class="card">
                    <h3>Login</h3>
                    <p>Sign in to manage reservations, view details, and generate bills.</p>
                    <div class="btnrow"><a class="btn primary" href="login.html">Login</a></div>
                  </div>

                  <div class="card">
                    <h3>Help</h3>
                    <p>Read instructions on how to use the system features.</p>
                    <div class="btnrow"><a class="btn" href="help.html">Open</a></div>
                  </div>
                </div>
                """;

        String welcomeLine = loggedIn
                ? "<div class='subtitle'>Welcome, <b>" + escape(username) + "</b></div>"
                : "<div class='subtitle'>Please login to access reservation features</div>";

        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Room Reservation System - Home</title>

              <style>
                :root{
                  --bg1:#0f172a;
                  --bg2:#111827;
                  --card: rgba(255,255,255,.07);
                  --border: rgba(255,255,255,.14);
                  --text: rgba(255,255,255,.92);
                  --muted: rgba(255,255,255,.68);
                  --accent:#3b82f6;
                  --accent2:#22c55e;
                  --danger:#ef4444;
                  --shadow: 0 18px 40px rgba(0,0,0,.45);
                  --radius: 18px;
                }

                *{box-sizing:border-box;}
                body{
                  margin:0;
                  font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
                  color:var(--text);
                  min-height:100vh;
                  padding:24px;
                  background:
                    radial-gradient(900px 500px at 15%% 20%%, rgba(59,130,246,.35), transparent 55%%),
                    radial-gradient(700px 420px at 85%% 25%%, rgba(34,197,94,.25), transparent 60%%),
                    radial-gradient(900px 600px at 50%% 90%%, rgba(168,85,247,.18), transparent 60%%),
                    linear-gradient(135deg, var(--bg1), var(--bg2));
                }

                .wrap{
                  width:min(1050px, 100%%);
                  margin:0 auto;
                  display:grid;
                  gap:16px;
                }

                .topbar{
                  display:flex;
                  align-items:center;
                  justify-content:space-between;
                  gap:12px;
                  padding:14px 16px;
                  border:1px solid var(--border);
                  background: rgba(255,255,255,.06);
                  backdrop-filter: blur(10px);
                  border-radius: var(--radius);
                  box-shadow: var(--shadow);
                }

                .brand{
                  display:flex;
                  align-items:center;
                  gap:10px;
                  font-weight:800;
                  letter-spacing:.2px;
                }
                .logo{
                  width:38px;height:38px;border-radius:12px;
                  background: linear-gradient(135deg, rgba(59,130,246,.95), rgba(34,197,94,.85));
                  box-shadow: 0 10px 22px rgba(59,130,246,.25);
                }
                .subtitle{
                  font-size:13px;
                  color:var(--muted);
                  font-weight:500;
                  margin-top:2px;
                }

                .pill{
                  display:inline-flex;
                  align-items:center;
                  gap:8px;
                  padding:10px 12px;
                  border:1px solid rgba(255,255,255,.16);
                  border-radius: 999px;
                  background: rgba(255,255,255,.06);
                  color: var(--muted);
                  font-size: 13px;
                  white-space:nowrap;
                }

                .grid{
                  display:grid;
                  grid-template-columns: repeat(3, 1fr);
                  gap:14px;
                }
                .grid2{
                  grid-template-columns: repeat(2, 1fr);
                }

                .card{
                  border:1px solid var(--border);
                  background: var(--card);
                  backdrop-filter: blur(10px);
                  border-radius: var(--radius);
                  box-shadow: var(--shadow);
                  padding: 18px;
                  transition: .2s ease;
                }
                .card:hover{
                  transform: translateY(-2px);
                  border-color: rgba(59,130,246,.35);
                }

                .card h3{
                  margin:0 0 6px 0;
                  font-size:18px;
                }
                .card p{
                  margin:0;
                  color:var(--muted);
                  font-size:14px;
                  line-height:1.45;
                }

                .btnrow{
                  margin-top:12px;
                  display:flex;
                  gap:10px;
                  align-items:center;
                  justify-content:flex-end;
                  flex-wrap:wrap;
                }

                a.btn{
                  text-decoration:none;
                  display:inline-flex;
                  align-items:center;
                  justify-content:center;
                  padding: 10px 12px;
                  border-radius: 14px;
                  font-weight:800;
                  font-size:14px;
                  border:1px solid rgba(255,255,255,.14);
                  background: rgba(255,255,255,.06);
                  color: var(--text);
                  transition: .2s ease;
                  min-width: 120px;
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
                  box-shadow: 0 14px 28px rgba(239,68,68,.18);
                }

                .footer{
                  text-align:center;
                  color: rgba(255,255,255,.55);
                  font-size:12px;
                  margin-top:6px;
                }

                @media (max-width: 900px){
                  .grid{ grid-template-columns: 1fr; }
                  .grid2{ grid-template-columns: 1fr; }
                  .topbar{ flex-direction:column; align-items:flex-start; }
                  .pill{ width:100%%; justify-content:space-between; }
                  .btnrow{ justify-content:flex-start; }
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
                      %s
                    </div>
                  </div>

                  <div class="pill">
                    <span>Java Servlets + MySQL</span>
                    <span>Native HTML/CSS/JS</span>
                  </div>
                </div>

                %s

                <div class="footer">© Room Reservation System • Home menu changes after login</div>
              </div>
            </body>
            </html>
        """.formatted(welcomeLine, cards);

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