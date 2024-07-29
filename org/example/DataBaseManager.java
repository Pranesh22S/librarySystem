package org.example;

import java.sql.*;

public class DataBaseManager {
    private String PWD="Pranesh_22";
    private String DBURL="jdbc:mysql://localhost:3306/LibraryManagementSystem";
    private String user="root";
    private Connection connection;
    public Connection getDBConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DBURL, user, PWD);
            if (this.connection != null) {
                System.out.println("Database connection established.");
            } else {
                System.out.println("Failed to establish database connection.");
            }
            return this.connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
