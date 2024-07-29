<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, javax.servlet.*, javax.servlet.http.*" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.Book" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library Management Student Dashboard</title>
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
        .content form {
            max-width: 600px;
            margin: 0 auto;
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .content form label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        .content form input[type="text"],
        .content form input[type="number"],
        .content form input[type="password"],
        .content form input[type="date"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #dddddd;
            border-radius: 4px;
        }
        .content form input[type="submit"] {
            background-color: #3498db;
            color: #ffffff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1.1em;
        }
        .content form input[type="submit"]:hover {
            background-color: #2980b9;
        }
        .content table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        .content table th, .content table td {
            border: 1px solid #dddddd;
            padding: 8px;
            text-align: left;
        }
        .content table th {
            background-color: #f2f2f2;
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
            <a href="?action=bookMenu">Book Menu</a>
            <a href="?action=orderBook">Order Book</a>
            <a href="?action=returnBook">Return Book</a>
            <a href="?action=viewBookInfo">View Book Info</a>
            <a href="?action=viewAccountInfo">View Account Info</a>
            <a href="?action=updateAccount">Update Account</a>
            <a href="?action=deleteAccount">Delete Account</a>
            <a href="?action=logout">Logout</a>
        </div>
        <div class="content">
            <%
                String action = request.getParameter("action");
                if (action == null || action.isEmpty()) {
                    out.println("<h2>Welcome to the Student Dashboard</h2>");
                    out.println("<p>Select an action from the sidebar to manage your library activities.</p>");
                } else {
                    switch (action) {
                        case "bookMenu":
                        %>
                        <form action="Studentservlet" method="post">
                            <input type="hidden" name="action" value="bookMenu">
                            <h1>Library Book List</h1>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Book Name</th>
                                        <th>Author</th>
                                        <th>Genre</th>
                                        <th>Count</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Book> books = (List<Book>) request.getAttribute("books");
                                        System.out.println("Books: " + books);
                                        if (books != null && !books.isEmpty()) {
                                            for (Book book : books) {
                                    %>
                                    <tr>
                                        <td><%= book.getBookName() %></td>
                                        <td><%= book.getAuthor() %></td>
                                        <td><%= book.getGenre() %></td>
                                        <td><%= book.getBooksCount() %></td>
                                    </tr>
                                    <%
                                            }
                                        } else {
                                    %>
                                    <tr>
                                        <td colspan="4">No books available.</td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                            <input type="submit" value="getBookMenu">
                        </form>
                        <%
                        break;
                        case "orderBook":
                        %>
                        <h2>Order Book</h2>
                        <form action="Studentservlet" method="post">
                            <input type="hidden" name="action" value="orderBook">
                            <label for="bookId">Book ID:</label>
                            <input type="text" id="bookId" name="bookId" required>
                            <label for="ReturnDate">Return Date:</label>
                            <input type="date" id="returndate" name="rdate" required>
                            <input type="submit" value="Order Book">
                        </form>
                        <%
                        break;
                        case "returnBook":
                        %>
                        <h2>Return Book</h2>
                        <form action="Studentservlet" method="post">
                            <input type="hidden" name="action" value="returnBook">
                            <label for="bookId">Book ID:</label>
                            <input type="text" id="bookId" name="bookId" required>
                            <input type="submit" value="Return Book">
                        </form>
                        <%
                        break;
                        case "viewBookInfo":
                        %>
                        <h2>View Book Information</h2>
                        <form action="Studentservlet" method="get">
                            <input type="hidden" name="action" value="viewBookInfo">
                            <label for="bookId">Book ID:</label>
                            <input type="text" id="bookId" name="bookId" required>
                            <input type="submit" value="View Book Info">
                            <p>BookName: ${bookname}</p>
                            <p>Auhtor: ${author}</p>
                            <p>genre: ${genre}</p>
                            <p>AvailableCount: ${numberOfAvailable}</p>
                        </form>
                        <%
                        break;
                        case "viewAccountInfo":
                        %>
                        <form action="Studentservlet" method="get">
                            <input type="hidden" name="action" value="viewAccountInfo">
                            <h2>Account Information</h2>
                            <p>Name: ${userName}</p>
                            <p>Mail: ${mail}</p>
                            <p>Number of Books Loaned: ${numberOfBooksLoaned}</p>
                            <p>Number of Books Returned: ${numberOfBooksReturned}</p>
                            <p>Fine Amount: ${fineAmount}</p>
                            <input type="submit" value="View Account Info">
                        </form>
                        <%
                        break;
                        case "updateAccount":
                        %>
                        <h2>Update Account</h2>
                        <form action="Studentservlet" method="post">
                            <input type="hidden" name="action" value="updateAccount">
                            <label for="newMail">New Mail:</label>
                            <input type="text" id="newMail" name="newMail" required>
                            <label for="newPassword">New Password:</label>
                            <input type="password" id="newPassword" name="newPassword" required>
                            <input type="submit" value="Update Account">
                        </form>
                        <%
                            break;
                            case "deleteAccount":
                             %>
                             <h2>Delete Account</h2>
                             <form action="studentervlet" method="post">
                             <input type="hidden" name="action" value="deleteAccount">
                             <label for="confirm">Enter YES to confirm account deletion:</label>
                             <input type="text" id="confirm" name="confirm" required>
                             <input type="submit" value="Delete Account">
                             </form>
                        <%
                        break;
                        case "logout":
                            response.sendRedirect("loginregister.jsp");
                            break;
                        default:
                            out.println("<p>Invalid action!</p>");
                            break;
                    }
                }
            %>
        </div>
    </div>
</body>
</html>
