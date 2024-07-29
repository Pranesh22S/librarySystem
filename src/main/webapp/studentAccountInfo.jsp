<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Account Information</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
            overflow: hidden;
        }
        .container {
            display: flex;
            width: 100%;
            height: 100%;
        }
        .sidebar {
            width: 250px;
            background-color: #2c3e50;
            color: #ecf0f1;
            padding: 20px;
            box-sizing: border-box;
            border-right: 2px solid #34495e;
        }
        .sidebar h2 {
            color: #ecf0f1;
            font-size: 1.5em;
            margin: 0 0 20px 0;
        }
        .sidebar a {
            display: block;
            margin-bottom: 15px;
            color: #ecf0f1;
            text-decoration: none;
            font-size: 1.1em;
            padding: 10px;
            border-radius: 5px;
            transition: background-color 0.3s, color 0.3s;
        }
        .sidebar a:hover {
            background-color: #34495e;
            color: #ffffff;
        }
        .content {
            flex-grow: 1;
            padding: 20px;
            box-sizing: border-box;
            background-color: #ffffff;
            overflow-y: auto;
        }
        .content h2 {
            color: #333333;
            margin-bottom: 20px;
        }
        .content p {
            font-size: 1.1em;
            color: #555555;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <h2>Student Dashboard</h2>
            <a href="studentDashboard.jsp?action=bookMenu">Book Menu</a>
            <a href="studentDashboard.jsp?action=orderBook">Order Book</a>
            <a href="studentDashboard.jsp?action=returnBook">Return Book</a>
            <a href="studentDashboard.jsp?action=viewBookInfo">View Book Info</a>
            <a href="studentDashboard.jsp?action=viewAccountInfo">View Account Info</a>
            <a href="studentDashboard.jsp?action=updateAccount">Update Account</a>
            <a href="studentDashboard.jsp?action=deleteAccount">Delete Account</a>
            <a href="studentDashboard.jsp?action=logout">Logout</a>
        </div>
        <div class="content">
            <h2>Account Information</h2>
            <p>Name: ${userName}</p>
            <p>Mail: ${mail}</p>
            <p>Number of Books Loaned: ${numberOfBooksLoaned}</p>
            <p>Number of Books Returned: ${numberOfBooksReturned}</p>
            <p>Fine Amount: ${fineAmount}</p>
        </div>
    </div>
</body>
</html>
