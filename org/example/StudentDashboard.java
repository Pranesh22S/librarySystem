package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Studentservlet")
public class StudentDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }

        String action = request.getParameter("action");

        try (Connection conn = new DataBaseManager().getDBConnection()) {
            String mail = (String) session.getAttribute("userMail");
            String studentId = getStudentId(mail, conn);

            if (studentId == null) {
                response.sendRedirect("error.jsp?error=Student not found.");
                return;
            }

            switch (action) {
                case "bookMenu":
                    listAvailableBooks(request, response, conn);
                    break;
                case "viewBookInfo":
                    viewBookInfo(request, response, conn);
                    break;
                case "viewAccountInfo":
                    viewAccountInfo(request, response, conn);
                    break;
                case "logout":
                    session.invalidate();
                    response.sendRedirect("loginregister.jsp");
                    break;
                default:
                    response.sendRedirect("studentDashboard.jsp");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Database error.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }

        String action = request.getParameter("action");

        try (Connection conn = new DataBaseManager().getDBConnection()) {
            String mail = (String) session.getAttribute("userMail");
            String studentId = getStudentId(mail, conn);

            if (studentId == null) {
                response.sendRedirect("error.jsp?error=Student not found.");
                return;
            }

            switch (action) {
                case "orderBook":
                    orderBook(request, response, conn, studentId);
                    break;
                case "returnBook":
                    returnBook(request, response, conn, studentId);
                    break;
                case "updateAccount":
                    updateAccount(request, response, conn, mail);
                    break;
                case "deleteAccount":
                    deleteAccount(request, response, conn, studentId);
                    break;
                default:
                    response.sendRedirect("studentDashboard.jsp");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Database error.");
        }
    }

    private String getStudentId(String mail, Connection connection) throws SQLException {
        String query = "SELECT user_id FROM user_db WHERE user_mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");
                }
            }
        }
        return null;
    }

    private void listAvailableBooks(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        try {
            List<Book> books = getAllBookDatabase(conn);
            request.setAttribute("books", books);
            request.getRequestDispatcher("/studentDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to list available books.");
        }
    }

    private List<Book> getAllBookDatabase(Connection conn) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM library_db";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getString("book_id"));
                book.setBookName(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setGenre(rs.getString("genre"));
                book.setBooksCount(rs.getInt("books_count"));
                books.add(book);
            }
        }
        return books;
    }

    private void viewBookInfo(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        String bookId = request.getParameter("bookId");
        if (bookId == null || bookId.isEmpty()) {
            response.sendRedirect("error.jsp?message=Please enter a book ID.");
            return;
        }
        String query = "SELECT * FROM library_db WHERE book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String book_name=rs.getString("book_name");
                    String author=rs.getString("author");
                    String genre=rs.getString("genre");
                    int countofBooks=rs.getInt("books_count");
                    request.setAttribute("bookname", book_name);
                    request.setAttribute("author", author);
                    request.setAttribute("genre", genre);
                    request.setAttribute("numberOfAvailable", countofBooks);
                    request.getRequestDispatcher("studentDashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("error.jsp?message=Book not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Database error.");
        }
    }

    private void viewAccountInfo(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }

        String mail = (String) session.getAttribute("userMail");
        if (mail == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }

        String query = "SELECT * FROM user_db WHERE user_mail = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String studentId = rs.getString("user_id");
                    String userName = rs.getString("user_name");
                    query = "SELECT * FROM student_library_activity WHERE student_id = ?";
                    try (PreparedStatement activityPstmt = conn.prepareStatement(query)) {
                        activityPstmt.setString(1, studentId);
                        try (ResultSet activityRs = activityPstmt.executeQuery()) {
                            if (activityRs.next()) {
                                int numberOfBooksLoaned = activityRs.getInt("number_of_books_borrowed");
                                int numberOfBooksReturned = activityRs.getInt("number_of_books_returned");
                                double fineAmount = activityRs.getDouble("fine_amount");

                                request.setAttribute("userName", userName);
                                request.setAttribute("mail", mail);
                                request.setAttribute("numberOfBooksLoaned", numberOfBooksLoaned);
                                request.setAttribute("numberOfBooksReturned", numberOfBooksReturned);
                                request.setAttribute("fineAmount", fineAmount);
                                request.getRequestDispatcher("studentDashboard.jsp").forward(request, response);
                            } else {
                                response.sendRedirect("error.jsp?message=Student activity not found.");
                            }
                        }
                    }
                } else {
                    response.sendRedirect("error.jsp?message=Student not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Database error.");
        }
    }

    private void orderBook(HttpServletRequest request, HttpServletResponse response, Connection connection, String studentId) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        LocalDate returnDate;
        String returnDateStr = request.getParameter("rdate");
        if (returnDateStr == null || returnDateStr.isEmpty()) {
            response.sendRedirect("error.jsp?message=Please enter a return date.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            returnDate = LocalDate.parse(returnDateStr, formatter);
        } catch (DateTimeParseException e) {
            response.sendRedirect("error.jsp?message=Invalid return date format.");
            return;
        }

        String currentDate = LocalDate.now().toString();
        String query = "INSERT INTO library_loans (Book_id, student_id, issueDate, DueDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, studentId);
            pstmt.setString(3, currentDate);
            pstmt.setString(4, returnDate.toString());
            pstmt.executeUpdate();

            query = "UPDATE library_db SET books_count = books_count - 1 WHERE book_id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(query)) {
                updateStmt.setString(1, bookId);
                updateStmt.executeUpdate();
            }

            response.sendRedirect("studentDashboard.jsp?success=Book ordered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to order book.");
        }
    }

    private void returnBook(HttpServletRequest request, HttpServletResponse response, Connection connection, String studentId) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        String query = "DELETE FROM library_loans WHERE Book_id = ? AND student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, studentId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                query = "UPDATE library_db SET books_count = books_count + 1 WHERE book_id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(query)) {
                    updateStmt.setString(1, bookId);
                    updateStmt.executeUpdate();
                }
                response.sendRedirect("studentDashboard.jsp?success=Book returned successfully.");
            } else {
                response.sendRedirect("error.jsp?message=No matching loan found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to return book.");
        }
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response, Connection connection, String mail) throws SQLException, IOException {
        String newPassword = request.getParameter("newPassword");
        String query = "UPDATE user_db SET user_password = ? WHERE user_mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, mail);
            pstmt.executeUpdate();
            response.sendRedirect("studentDashboard.jsp?success=Account updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to update account.");
        }
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response, Connection connection, String studentId) throws SQLException, IOException {
        String query = "DELETE FROM user_db WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            response.sendRedirect("loginregister.jsp?success=Account deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to delete account.");
        }
    }
}
