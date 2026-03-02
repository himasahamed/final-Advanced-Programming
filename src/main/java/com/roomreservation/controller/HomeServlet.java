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

        // ✅ Keep existing content + links exactly the same
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
                    <h3>View Reports</h3>
                    <p>See all reservations in a report table with quick View/Bill actions.</p>
                    <div class="btnrow"><a class="btn primary" href="reports.html">Open</a></div>
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
                ? "<div class='welcome'>Welcome, <b>" + escape(username) + "</b></div>"
                : "<div class='welcome'>Please login to access reservation features</div>";

        // ✅ UI changed only + hero text updated with your provided sentences
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Room Reservation System - Home</title>

              <style>
                :root{
                  /* ===== Friendly Light Theme (Blue + Soft) ===== */
                  --bg1:#f5f8ff;
                  --bg2:#eef6ff;
                  --bg3:#ffffff;

                  --primary:#2563eb;     /* blue */
                  --primary2:#06b6d4;    /* cyan accent */
                  --primary3:#3b82f6;    /* lighter blue */

                  --text:#0f172a;        /* dark slate */
                  --muted:#475569;       /* gray text */
                  --muted2:#64748b;

                  --card:#ffffff;
                  --card2:#f8fafc;

                  --border: rgba(15,23,42,.12);
                  --shadow: 0 14px 30px rgba(2, 8, 23, .08);

                  --radius: 18px;
                  --radius2: 22px;

                  --danger1:#ef4444;
                  --danger2:#fb7185;
                }

                *{ box-sizing:border-box; }

                body{
                  margin:0;
                  font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
                  color:var(--text);
                  min-height:100vh;
                  background:
                    radial-gradient(900px 520px at 15%% 10%%, rgba(37,99,235,.18), transparent 55%%),
                    radial-gradient(900px 520px at 85%% 15%%, rgba(6,182,212,.18), transparent 60%%),
                    linear-gradient(135deg, var(--bg1), var(--bg2));
                  position:relative;
                  overflow-x:hidden;
                }

                /* subtle grid overlay */
                body:before{
                  content:"";
                  position:fixed;
                  inset:0;
                  background:
                    linear-gradient(rgba(15,23,42,.05) 1px, transparent 1px),
                    linear-gradient(90deg, rgba(15,23,42,.05) 1px, transparent 1px);
                  background-size: 64px 64px;
                  mask-image: radial-gradient(ellipse at 30%% 10%%, rgba(0,0,0,.90), transparent 60%%);
                  pointer-events:none;
                  opacity:.35;
                }

                .page{
                  width:min(1180px, 100%%);
                  margin:0 auto;
                  padding:22px;
                  display:grid;
                  gap:18px;
                }

                /* ===== Navbar ===== */
                .nav{
                  display:flex;
                  align-items:center;
                  justify-content:space-between;
                  gap:14px;
                  padding:14px 16px;
                  border:1px solid var(--border);
                  background: rgba(255,255,255,.78);
                  backdrop-filter: blur(10px);
                  border-radius: var(--radius2);
                  box-shadow: var(--shadow);
                }

                .brand{
                  display:flex;
                  align-items:center;
                  gap:12px;
                  min-width: 280px;
                }

                .mark{
                  width:44px;
                  height:44px;
                  border-radius: 14px;
                  background: linear-gradient(135deg, var(--primary), var(--primary2));
                  box-shadow: 0 14px 30px rgba(37,99,235,.22);
                }

                .brandText{
                  display:flex;
                  flex-direction:column;
                  gap:2px;
                }

                .brandTitle{
                  font-weight:900;
                  letter-spacing:.2px;
                  font-size: 16px;
                  line-height:1.1;
                  color: var(--text);
                }

                .welcome{
                  font-size: 13px;
                  color: var(--muted);
                  font-weight:600;
                }

                .navRight{
                  display:flex;
                  align-items:center;
                  gap:10px;
                  flex-wrap:wrap;
                  justify-content:flex-end;
                }

                .chip{
                  padding:10px 12px;
                  border-radius: 999px;
                  border:1px solid rgba(15,23,42,.10);
                  background: rgba(37,99,235,.06);
                  color: rgba(15,23,42,.85);
                  font-size: 13px;
                  white-space:nowrap;
                }

                /* ===== Hero ===== */
                .hero{
                  display:grid;
                  grid-template-columns: 1.15fr .85fr;
                  gap:16px;
                  align-items:stretch;
                }

                .heroLeft{
                  border:1px solid var(--border);
                  background: rgba(255,255,255,.80);
                  border-radius: var(--radius2);
                  box-shadow: var(--shadow);
                  padding: 20px;
                  position:relative;
                  overflow:hidden;
                }

                .heroLeft:before{
                  content:"";
                  position:absolute;
                  inset:-40px -80px auto auto;
                  width:280px;
                  height:280px;
                  border-radius: 999px;
                  background: radial-gradient(circle at 30%% 30%%, rgba(6,182,212,.25), transparent 62%%);
                  opacity:1;
                  pointer-events:none;
                }

                .heroTitle{
                  margin:0;
                  font-size: 44px;
                  line-height:1.05;
                  letter-spacing:-.6px;
                  color: var(--text);
                }

                .heroDesc{
                  margin:10px 0 0 0;
                  color: var(--muted);
                  font-size: 14px;
                  line-height: 1.55;
                  max-width: 62ch;
                }

                .heroRight{
                  border:1px solid var(--border);
                  background: rgba(255,255,255,.72);
                  border-radius: var(--radius2);
                  box-shadow: var(--shadow);
                  padding: 16px;
                  position:relative;
                  overflow:hidden;
                }

                /* decorative “building” */
                .tower{
                  position:absolute;
                  right:-26px;
                  bottom:-30px;
                  width: 260px;
                  height: 420px;
                  border-radius: 40px;
                  background:
                    linear-gradient(180deg, rgba(37,99,235,.16), rgba(255,255,255,.50)),
                    repeating-linear-gradient(
                      90deg,
                      rgba(15,23,42,.08) 0px,
                      rgba(15,23,42,.08) 8px,
                      rgba(15,23,42,.03) 8px,
                      rgba(15,23,42,.03) 22px
                    );
                  border:1px solid rgba(15,23,42,.12);
                  box-shadow: 0 30px 70px rgba(2, 8, 23, .10);
                  transform: rotate(-8deg);
                  opacity:.95;
                }

                .floatCard{
                  position:absolute;
                  left:16px;
                  top:16px;
                  width: 220px;
                  border-radius: 18px;
                  border:1px solid rgba(15,23,42,.10);
                  background: rgba(255,255,255,.80);
                  padding: 12px;
                  box-shadow: 0 22px 60px rgba(2, 8, 23, .10);
                }

                .floatTop{
                  display:flex;
                  align-items:center;
                  justify-content:space-between;
                  gap:10px;
                  margin-bottom:10px;
                }

                .miniPic{
                  width:54px;height:40px;border-radius:12px;
                  background: linear-gradient(135deg, rgba(37,99,235,.95), rgba(6,182,212,.85));
                  border:1px solid rgba(15,23,42,.10);
                }

                .heart{
                  width:28px;height:28px;border-radius: 999px;
                  display:flex;align-items:center;justify-content:center;
                  border:1px solid rgba(15,23,42,.10);
                  background: rgba(37,99,235,.06);
                  font-size:14px;
                }

                .floatText{
                  margin:0;
                  color: rgba(15,23,42,.80);
                  font-size: 13px;
                  line-height:1.35;
                }

                /* ===== Content + Cards ===== */
                .content{
                  border:1px solid var(--border);
                  background: rgba(255,255,255,.78);
                  border-radius: var(--radius2);
                  box-shadow: var(--shadow);
                  padding: 16px;
                }

                .grid{
                  display:grid;
                  grid-template-columns: repeat(3, 1fr);
                  gap:14px;
                }
                .grid2{ grid-template-columns: repeat(2, 1fr); }

                .card{
                  border:1px solid rgba(15,23,42,.10);
                  background: var(--card);
                  border-radius: 20px;
                  padding: 18px;
                  box-shadow: 0 16px 40px rgba(2, 8, 23, .06);
                  transition: transform .18s ease, border-color .18s ease, box-shadow .18s ease;
                  position:relative;
                  overflow:hidden;
                }

                .card:before{
                  content:"";
                  position:absolute;
                  inset:auto -60px -80px auto;
                  width:220px;height:220px;
                  border-radius:999px;
                  background: radial-gradient(circle at 30%% 30%%, rgba(37,99,235,.14), transparent 62%%);
                  opacity:1;
                  pointer-events:none;
                }

                .card:hover{
                  transform: translateY(-2px);
                  border-color: rgba(37,99,235,.35);
                  box-shadow: 0 18px 46px rgba(2, 8, 23, .10);
                }

                .card h3{
                  margin:0 0 8px 0;
                  font-size:18px;
                  letter-spacing:.1px;
                  color: var(--text);
                }

                .card p{
                  margin:0;
                  color: var(--muted);
                  font-size: 14px;
                  line-height: 1.45;
                }

                .btnrow{
                  margin-top:14px;
                  display:flex;
                  justify-content:flex-end;
                  align-items:center;
                  gap:10px;
                }

                a.btn{
                  text-decoration:none;
                  display:inline-flex;
                  align-items:center;
                  justify-content:center;
                  padding: 10px 14px;
                  border-radius: 999px;
                  font-weight: 800;
                  font-size: 14px;
                  border:1px solid rgba(15,23,42,.10);
                  background: rgba(37,99,235,.06);
                  color: rgba(15,23,42,.90);
                  transition: transform .18s ease, background .18s ease, border-color .18s ease;
                  min-width: 120px;
                }

                a.btn:hover{
                  transform: translateY(-1px);
                  border-color: rgba(37,99,235,.35);
                  background: rgba(37,99,235,.10);
                }

                a.btn.primary{
                  border:none;
                  color: #fff;
                  background: linear-gradient(135deg, var(--primary), var(--primary2));
                  box-shadow: 0 14px 26px rgba(37,99,235,.18);
                }

                a.btn.primary:hover{
                  background: linear-gradient(135deg, rgba(37,99,235,1), rgba(6,182,212,1));
                }

                a.btn.danger{
                  border:none;
                  color: #fff;
                  background: linear-gradient(135deg, rgba(239,68,68,.95), rgba(251,113,133,.90));
                  box-shadow: 0 14px 26px rgba(239,68,68,.14);
                }

                .footer{
                  text-align:center;
                  color: rgba(15,23,42,.55);
                  font-size: 12px;
                  padding: 6px 0 2px;
                }

                @media (max-width: 980px){
                  .hero{ grid-template-columns: 1fr; }
                  .tower{ right:-46px; }
                }

                @media (max-width: 900px){
                  .grid{ grid-template-columns: 1fr; }
                  .grid2{ grid-template-columns: 1fr; }
                  .nav{ flex-direction:column; align-items:flex-start; }
                  .navRight{ width:100%%; justify-content:flex-start; }
                  .heroTitle{ font-size: 34px; }
                  .btnrow{ justify-content:flex-start; }
                  a.btn{ width:100%%; }
                }
              </style>
            </head>

            <body>
              <div class="page">

                <header class="nav">
                  <div class="brand">
                    <div class="mark"></div>
                    <div class="brandText">
                      <div class="brandTitle">Room Reservation System</div>
                      %s
                    </div>
                  </div>

                  <div class="navRight">
                    <div class="chip">Java Servlets + MySQL</div>
                    <div class="chip">Native HTML/CSS/JS</div>
                  </div>
                </header>

                <section class="hero">
                  <div class="heroLeft">
                    <h1 class="heroTitle">Welcome Home</h1>
                    <p class="heroDesc">
                      Your effortless path to the perfect stay. The best rates for your next escape.<br>
                      Worldwide hotels, handpicked for you.
                    </p>
                  </div>

                  <div class="heroRight" aria-hidden="true">
                    <div class="floatCard">
                      <div class="floatTop">
                        <div class="miniPic"></div>
                        <div class="heart">♥</div>
                      </div>
                      <p class="floatText">Friendly UI • Clean card layout • Fast navigation</p>
                    </div>
                    <div class="tower"></div>
                  </div>
                </section>

                <main class="content">
                  %s
                </main>

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