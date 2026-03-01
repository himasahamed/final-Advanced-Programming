package com.roomreservation.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection conn;

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3307/room_reservation_db?useSSL=false&serverTimezone=UTC",
                        "root",
                        "@#Hima1@#"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}