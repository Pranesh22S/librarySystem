package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static Logger l = Logger.getLogger(UserServlet.class.getName());
    private IdentityAccessManagement iam = new IdentityAccessManagement();
    private DataBaseManager dbManager = new DataBaseManager();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        l.info("Action: " + action);

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("register".equals(action)) {
            handleRegister(request, response);
        }
    }
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        try (Connection conn = dbManager.getDBConnection()) {
            if (iam.authenticateUser(conn, email, password, userType)) {
                HttpSession session = request.getSession();
                session.setAttribute("userMail", email);
                if ("librarian".equalsIgnoreCase(userType)) {
                    response.sendRedirect("librarianDashboard.jsp");
                } else if ("student".equalsIgnoreCase(userType)) {
                    response.sendRedirect("studentDashboard.jsp");
                }
            } else {
                response.sendRedirect("loginregister.jsp?error=Invalid credentials");
            }
        } catch (Exception e) {
            l.severe("Error during login: " + e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        try (Connection conn = dbManager.getDBConnection()) {
            if (conn == null) {
                l.severe("Error in MySQL connection");
                response.sendRedirect("error.jsp");
                return;
            }
            iam.loadToUserDataBase(userType, name, email, password, conn, response);
            response.sendRedirect("loginregister.jsp");
        } catch (Exception e) {
            l.severe("Error during registration: " + e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Servlet is working!</h1>");
    }
}
