package org.example;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class IdentityAccessManagement {
    private static final int CIPHER_KEY = 2;
    private static final int ASCII_START = 31;
    private static final int ASCII_END = 'z';
    private String userId;
    public String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= ASCII_START && ch <= ASCII_END) {
                int shifted = ch + CIPHER_KEY;
                if (shifted > ASCII_END) {
                    shifted = ASCII_START + (shifted - ASCII_END - 1);
                }
                encrypted.append((char) shifted);
            } else {
                encrypted.append(ch);
            }
        }
        return encrypted.toString();
    }

    public String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= ASCII_START && ch <= ASCII_END) {
                int shifted = ch - CIPHER_KEY;
                if (shifted < ASCII_START) {
                    shifted = ASCII_END - (ASCII_START - shifted - 1);
                }
                decrypted.append((char) shifted);
            } else {
                decrypted.append(ch);
            }
        }
        return decrypted.toString();
    }
    public boolean authenticateUser(Connection conn, String mail, String password, String userType) {
        if ("librarian".equalsIgnoreCase(userType)) {
            String query = "SELECT user_password FROM user_db WHERE user_mail = ? AND user_type = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, mail);
                pstmt.setString(2, userType);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String encryptedPassword = rs.getString("user_password");
                        String decryptedPassword = decrypt(encryptedPassword);
                        return decryptedPassword.equals(password);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        if(userType.equalsIgnoreCase("student")){
            System.out.println("HS");
            String query = "SELECT user_password FROM user_db WHERE user_mail = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, mail);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String encryptedPassword = rs.getString("user_password");
                        String decryptedPassword = decrypt(encryptedPassword);
                        return decryptedPassword.equals(password);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void loadToUserDataBase(String userType, String userName, String userMail,String userPassword, Connection conn, HttpServletResponse res) {
        if (userType == null || userName == null || userPassword == null || userMail == null || conn == null || res == null) {
            System.out.println("Null parameter detected in loadToUserDataBase.");
            return;
        }

        String userId = (userType.equalsIgnoreCase("librarian") ? "LB" : "ST") + new Random().nextInt(1000);
        if (isEmailExists(conn, userMail, userType)) {
            try {
                res.sendRedirect("register.jsp?error=Email already exists");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String insertUserQuery = "INSERT INTO user_db (user_id, user_type, user_name, user_mail, user_password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement psmt = conn.prepareStatement(insertUserQuery)) {
            psmt.setString(1, userId);
            psmt.setString(2, userType);
            psmt.setString(3, userName);
            psmt.setString(4, userMail);
            psmt.setString(5, encrypt(userPassword));
            psmt.executeUpdate();
            System.out.println("Registered Successfully");

            String insertActivityQuery;
            if (userType.equalsIgnoreCase("librarian")) {
                insertActivityQuery = "INSERT INTO librarian_activity (librarian_id, number_of_books_added, number_of_books_removed) VALUES (?, 0, 0)";
            } else {
                insertActivityQuery = "INSERT INTO student_library_activity (student_id, number_of_books_borrowed, number_of_books_returned, fine_amount) VALUES (?, 0, 0, 0.00)";
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insertActivityQuery)) {
                pstmt.setString(1, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in inserting user or activity record");
        }
    }

    private boolean isEmailExists(Connection conn, String userMail, String userType) {
        if (conn == null || userMail == null || userType == null) {
            System.out.println("Null parameter detected in isEmailExists.");
            return false;
        }
        String query = "SELECT COUNT(*) FROM user_db WHERE user_mail = ? AND user_type = ?";
        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setString(1, userMail);
            psmt.setString(2, userType);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Exception occurred while checking email.");
        }
        return false;
    }
}
