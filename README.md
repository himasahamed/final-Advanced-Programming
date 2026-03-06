README for Room Reservation System
Project Overview

The Room Reservation System is a web-based application developed for Ocean View Resort. The system allows hotel staff to:

Login securely to access the system.

Create new reservations by storing guest details, room type, and booking dates.

View and search reservation details by reservation number.

Generate and print bills for reservations based on the number of nights and room rates.

This project was developed as part of an advanced programming assignment and demonstrates key concepts such as servlets, database integration, and version control with Git and GitHub.

Features

Login functionality: Allows authorized users (staff) to log in using username and password.

Add Reservation: Staff can create new reservations by entering guest details, room type, and booking dates.

View Reservation: View and search reservation details by reservation number.

Generate Bill: Calculate and print the bill based on room type, number of nights, and rate per night.

Help Section: Provides a guide on how to use the system for new staff.

Technologies Used

Frontend: HTML, CSS, and JavaScript for the user interface.

Backend: Java servlets (Jakarta EE).

Database: MySQL for storing reservation data and billing information.

Version Control: Git and GitHub for tracking changes and collaboration.

Setup Instructions
Prerequisites

Java 17 or later installed on your system.

Apache Tomcat 10.1.x or later installed as the servlet container.

MySQL installed and running on your local machine.

Steps to Set Up the Project Locally

Clone the Repository:
To clone the repository to your local machine, run the following command in your terminal:
git clone https://github.com/himasahamed/final-Advanced-Programming.git
Database Setup:

Create a MySQL database for the project:

CREATE DATABASE room_reservation_db;

Run the SQL script to create the required tables in the database:

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE reservations (
  reservation_no INT AUTO_INCREMENT PRIMARY KEY,
  guest_name VARCHAR(255) NOT NULL,
  guest_address VARCHAR(255),
  contact_number VARCHAR(15),
  room_type VARCHAR(50),
  check_in_date DATE,
  check_out_date DATE
);

CREATE TABLE bills (
  bill_id INT AUTO_INCREMENT PRIMARY KEY,
  reservation_no INT,
  nights INT,
  rate_per_night DECIMAL(10,2),
  total_amount DECIMAL(10,2),
  generated_date DATE,
  FOREIGN KEY (reservation_no) REFERENCES reservations(reservation_no)
);

Configure Database Connection:

Open the DBConnection.java file and update the database connection details (username and password) for your local MySQL setup.

Example:

Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/room_reservation_db",
    "root", "yourpassword");

Deploy the Project to Tomcat:

Open IntelliJ IDEA and deploy the project to Apache Tomcat.

Run the project by clicking the Run button in IntelliJ or by using Tomcat's startup.bat if you are running it manually.

Access the Application:

After successfully deploying, open a browser and navigate to:

http://localhost:8080/RoomReservationSystem
Usage Instructions

Login:
Use the following credentials to log in:

Username: admin

Password: admin123

Add a Reservation:

After logging in, navigate to the "Add Reservation" page and enter the guest details and reservation dates. The system will generate a unique reservation number upon submission.

View Reservation Details:

On the "View Reservation" page, you can search for a reservation using the reservation number. The details of the reservation will be displayed.

Generate Bill:

Navigate to the "Generate Bill" page, where you can enter a reservation number and generate the bill for the stay based on room rates and number of nights
