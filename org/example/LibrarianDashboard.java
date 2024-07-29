package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LibrarianServlet")
public class LibrarianDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println(action);
        HttpSession session = request.getSession();
        String mail = (String) session.getAttribute("userMail");

        if (mail == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }
        try (Connection connection = new DataBaseManager().getDBConnection()) {
            switch (action) {
                case "viewAccountInfo":
                    viewAccountInfo(request, response, connection, mail);
                    break;
                case "checkDues":
                    checkDues(request, response, connection);
                    break;
                default:
                    response.sendRedirect("librarianDashboard.jsp");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String mail = (String) session.getAttribute("userMail");
        if (mail == null) {
            response.sendRedirect("login.jsp?error=Session expired, please login again.");
            return;
        }
        try (Connection connection = new DataBaseManager().getDBConnection()) {
            String librarianId = getLibrarianId(connection, mail);

            switch (action) {
                case "addBook":
                    addBook(request, response, connection, librarianId);
                    break;
                case "removeBook":
                    removeBook(request, response, connection, librarianId);
                    break;
                case "updateBook":
                    updateBook(request, response, connection);
                    break;
                case "deleteAccount":
                    deleteAccount(request, response, connection, mail);
                    break;
                case "updateAccount":
                    updateAccount(request, response, connection, mail);
                    break;
                case "logout":
                    request.getSession().invalidate();
                    response.sendRedirect("login.jsp");
                    return;
                default:
                    response.sendRedirect("librarianDashboard.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private String getLibrarianId(Connection connection, String mail) throws SQLException {
        String query = "SELECT user_id FROM user_db WHERE user_mail = ?";
        try (PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setString(1, mail);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");
                }
                return null;
            }
        }
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response, Connection connection, String librarianId) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        String bookName = request.getParameter("bookName");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String genre = request.getParameter("genre");
        int booksCount = Integer.parseInt(request.getParameter("booksCount"));
        String query = "INSERT INTO library_db (book_id, book_name, author, isbn, genre, books_count, librarian_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, bookName);
            pstmt.setString(3, author);
            pstmt.setString(4, isbn);
            pstmt.setString(5, genre);
            pstmt.setInt(6, booksCount);
            pstmt.setString(7, librarianId);
            pstmt.executeUpdate();
            updateBooksAdded(connection, librarianId);
        }
        response.sendRedirect("librarianDashboard.jsp");
    }

    private void removeBook(HttpServletRequest request, HttpServletResponse response, Connection connection, String librarianId) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        String query = "DELETE FROM library_db WHERE book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            pstmt.executeUpdate();
            updateBooksRemoved(connection, librarianId);
        }
        response.sendRedirect("librarianDashboard.jsp");
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException, IOException {
        String bookId = request.getParameter("bookId");
        String bookName = request.getParameter("bookName");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String genre = request.getParameter("genre");
        int booksCount = Integer.parseInt(request.getParameter("booksCount"));
        String query = "UPDATE library_db SET book_name = ?, author = ?, isbn = ?, genre = ?, books_count = ? WHERE book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, bookName);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setString(4, genre);
            pstmt.setInt(5, booksCount);
            pstmt.setString(6, bookId);
            pstmt.executeUpdate();
        }
        response.sendRedirect("librarianDashboard.jsp");
    }

    private void viewAccountInfo(HttpServletRequest request, HttpServletResponse response, Connection connection, String mail) throws ServletException, IOException {
        String name = "";
        int numberOfBooksAdded = 0;
        int numberOfBooksRemoved = 0;
        String userQuery = "SELECT user_name FROM user_db WHERE user_mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(userQuery)) {
            pstmt.setString(1, mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("user_name");
                } else {
                    System.out.println("Librarian not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while loading librarian details.");
            e.printStackTrace();
        }
        String activityQuery = "SELECT number_of_books_added, number_of_books_removed FROM librarian_activity WHERE librarian_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(activityQuery)) {
            pstmt.setString(1, getLibrarianId(connection, mail));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    numberOfBooksAdded = rs.getInt("number_of_books_added");
                    numberOfBooksRemoved = rs.getInt("number_of_books_removed");
                } else {
                    System.out.println("Librarian Activity not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while loading librarian activity details.");
            e.printStackTrace();
        }
        request.setAttribute("userName", name);
        request.setAttribute("mail", mail);
        request.setAttribute("numberOfBooksAdded", numberOfBooksAdded);
        request.setAttribute("numberOfBooksRemoved", numberOfBooksRemoved);
        request.getRequestDispatcher("librarianDashboard.jsp").forward(request, response);
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response, Connection connection, String mail) throws SQLException, IOException {
        String query = "DELETE FROM user_db WHERE user_mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, mail);
            pstmt.executeUpdate();
        }
        request.getSession().invalidate();
        response.sendRedirect("loginregister.jsp");
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response, Connection connection, String mail) throws SQLException, IOException {
        String newMail = request.getParameter("newMail");
        String newPassword = new IdentityAccessManagement().encrypt(request.getParameter("newPassword"));
        String query = "UPDATE user_db SET user_mail = ?, user_password = ? WHERE user_mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newMail);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, mail);
            pstmt.executeUpdate();
            response.sendRedirect("librarianDashboard.jsp");
        }
        request.getSession().setAttribute("userMail", newMail);
    }
    public void checkDues(HttpServletRequest request, HttpServletResponse response, Connection connection) throws SQLException,IOException {
        String query = "SELECT loan_id, Book_id, student_id, DueDate, returnDate FROM library_loans";
        LocalDate currentDate = LocalDate.now();
        List<Student> studentdueList=new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int loanId = rs.getInt("loan_id");
                String bookId = rs.getString("Book_id");
                String studentId = rs.getString("student_id");
                LocalDate dueDate = rs.getDate("DueDate").toLocalDate();
                LocalDate returnDate = rs.getDate("returnDate") != null ? rs.getDate("returnDate").toLocalDate() : null;
                if (returnDate == null && currentDate.isAfter(dueDate) || (returnDate != null && returnDate.isAfter(dueDate))) {
                    long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate != null ? returnDate : currentDate);
                    double fineAmount = calculateFine(daysOverdue);
                    assignFineToStudent(studentId, fineAmount, connection);
                    System.out.println("Assigned fine of " + fineAmount + " to student ID " + studentId + " for overdue loan ID " + loanId + " (Book ID: " + bookId + ")");
                    studentdueList.add(new Student(studentId,"",fineAmount));
                }
            }
            request.getRequestDispatcher("/librarianDashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while checking dues.");
            e.printStackTrace();
        }
        catch(ServletException e){
            e.printStackTrace();
        }
    }
    private double calculateFine(long daysOverdue) {
        double finePerDay = 15.0;
        return daysOverdue * finePerDay;
    }
    private void assignFineToStudent(String studentId, double fineAmount, Connection connection) {
        String updateQuery = "UPDATE student_library_activity SET fine_amount = fine_amount + ? WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, fineAmount);
            pstmt.setString(2, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while assigning fine to student.");
            e.printStackTrace();
        }
    }
    private void updateBooksAdded(Connection connection, String librarianId) throws SQLException {
        String query = "UPDATE librarian_db SET number_of_books_added = number_of_books_added + 1 WHERE librarian_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, librarianId);
            pstmt.executeUpdate();
        }
    }

    private void updateBooksRemoved(Connection connection, String librarianId) throws SQLException {
        String query = "UPDATE librarian_db SET number_of_books_removed = number_of_books_removed + 1 WHERE librarian_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, librarianId);
            pstmt.executeUpdate();
        }
    }
}
