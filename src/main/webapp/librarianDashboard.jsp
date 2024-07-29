<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, javax.servlet.*, javax.servlet.http.*, java.util.List, org.example.Student" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library Management Dashboard</title>
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
        .content form input[type="password"] {
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
        .content p {
            font-size: 1.1em;
            color: #555555;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <h2>Dashboard</h2>
            <a href="?action=addBook">Add Book</a>
            <a href="?action=addExistingBook">Add Existing Book</a>
            <a href="?action=removeBook">Remove Book</a>
            <a href="?action=updateBook">Update Book</a>
            <a href="?action=viewAccountInfo">View Account Info</a>
            <a href="?action=deleteAccount">Delete Account</a>
            <a href="?action=updateAccount">Update Account</a>
            <a href="?action=checkDues">Check Dues and Assign Fines</a>
            <a href="?action=logout">Logout</a>
        </div>
        <div class="content">
            <%
                String action = request.getParameter("action");

                if (action == null || action.isEmpty()) {
                    out.println("<h2>Welcome to the Library Management Dashboard</h2>");
                    out.println("<p>Select an action from the sidebar to manage the library.</p>");
                } else {
                    switch (action) {
                        case "addBook":
                            %>
                            <h2>Add Book</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="addBook">
                                <label for="bookId">Book ID:</label>
                                <input type="text" id="bookId" name="bookId" required>

                                <label for="bookName">Book Name:</label>
                                <input type="text" id="bookName" name="bookName" required>

                                <label for="author">Author:</label>
                                <input type="text" id="author" name="author" required>

                                <label for="isbn">ISBN:</label>
                                <input type="text" id="isbn" name="isbn" required>

                                <label for="genre">Genre:</label>
                                <input type="text" id="genre" name="genre" required>

                                <label for="booksCount">Number of Copies:</label>
                                <input type="number" id="booksCount" name="booksCount" required>

                                <input type="submit" value="Add Book">
                            </form>
                            <%
                            break;
                        case "addExistingBook":
                            %>
                            <h2>Add Existing Book</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="addExistingBook">
                                <label for="bookId">Book ID:</label>
                                <input type="text" id="bookId" name="bookId" required>

                                <label for="additionalCopies">Number of Copies to Add:</label>
                                <input type="number" id="additionalCopies" name="additionalCopies" required>

                                <input type="submit" value="Add Copies">
                            </form>
                            <%
                            break;
                        case "removeBook":
                            %>
                            <h2>Remove Book</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="removeBook">
                                <label for="bookId">Book ID:</label>
                                <input type="text" id="bookId" name="bookId" required>

                                <input type="submit" value="Remove Book">
                            </form>
                            <%
                            break;
                        case "updateBook":
                            %>
                            <h2>Update Book</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="updateBook">
                                <label for="bookId">Book ID:</label>
                                <input type="text" id="bookId" name="bookId" required>

                                <label for="bookName">Book Name:</label>
                                <input type="text" id="bookName" name="bookName">

                                <label for="author">Author:</label>
                                <input type="text" id="author" name="author">

                                <label for="isbn">ISBN:</label>
                                <input type="text" id="isbn" name="isbn">

                                <label for="genre">Genre:</label>
                                <input type="text" id="genre" name="genre">

                                <label for="booksCount">Number of Copies:</label>
                                <input type="number" id="booksCount" name="booksCount">

                                <input type="submit" value="Update Book">
                            </form>
                            <%
                            break;
                        case "viewAccountInfo":
                            %>
                            <h2>Account Information</h2>
                            <form action="LibrarianServlet" method="get">
                            <input type="hidden" name="action" value="viewAccountInfo">
                            <p>Name: ${userName}</p>
                            <p>Mail: ${mail}</p>
                            <p>Number of Books Added: ${numberOfBooksAdded}</p>
                            <p>Number of Books Removed: ${numberOfBooksRemoved}</p>
                             <input type="submit" value="getAccountInfo">
                            </form>
                            <%
                            break;
                        case "deleteAccount":
                            %>
                            <h2>Delete Account</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="deleteAccount">
                                <label for="confirm">Enter YES to confirm account deletion:</label>
                                <input type="text" id="confirm" name="confirm" required>

                                <input type="submit" value="Delete Account">
                            </form>
                            <%
                            break;
                        case "updateAccount":
                            %>
                            <h2>Update Account</h2>
                            <form action="LibrarianServlet" method="post">
                                <input type="hidden" name="action" value="updateAccount">
                                <label for="newMail">New Mail:</label>
                                <input type="text" id="newMail" name="newMail" required>
                                <label for="newPassword">New Password:</label>
                                <input type="password" id="newPassword" name="newPassword" required>
                                <input type="submit" value="Update Account">
                            </form>
                            <%
                            break;
                        case "checkDues":
                            %>
                            <form action="LibrarianServlet" method="get">
                            <input type="hidden" name="action" value="checkDues">
                            <h2>Check Dues and Assign Fines</h2>
                            <input type="submit" value="Check and Assign Fines">
                            <%
                            List<Student> studentDuesList = (List<Student>) request.getAttribute("studentDuesList");
                            if (studentDuesList != null && !studentDuesList.isEmpty()) {
                                %>
                                <table border="1">
                                <thead>
                                <tr>
                                <th>Student ID</th>
                                <th>Student Name</th>
                                <th>Fine Amount</th>
                                </tr>
                                </thead>
                                       <tbody>
                                              <%
                                       for (Student studentDue : studentDuesList) {
                                        %>
                                        <tr>
                                        <td><%= studentDue.getStudentID() %></td>
                                        <td><%= studentDue.getStudentName() %></td>
                                        <td><%= studentDue.getFineAmount() %></td>
                                        </tr>
                                         <%
                                                }
                                              %>
                                         </tbody>
                                     </table>
                                   </form>
                                <%
                            } else {
                                %>
                                <p>No dues found.</p>
                                <%
                            }
                            %>
                            <%
                            break;
                        case "logout":
                            response.sendRedirect("login.jsp");
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
