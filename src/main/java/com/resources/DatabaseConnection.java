package com.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    private static String url;
    private static String user;
    private static String password;
    
    static {
        // Railway environment variables
        url = System.getenv("JDBC_DATABASE_URL");
        user = System.getenv("JDBC_DATABASE_USERNAME");
        password = System.getenv("JDBC_DATABASE_PASSWORD");
        
        // Fallback for local development
        if (url == null || url.isEmpty()) {
            url = "jdbc:mysql://localhost:3306/laundry_db";
            System.out.println("⚠️ Using default DB URL: " + url);
        }
        if (user == null || user.isEmpty()) {
            user = "root";
            System.out.println("⚠️ Using default DB User: root");
        }
        if (password == null || password.isEmpty()) {
            password = "";
            System.out.println("⚠️ Using default DB Password: (empty)");
        }
        
        System.out.println("=========================================");
        System.out.println("📌 Database Connection Details:");
        System.out.println("   URL: " + url);
        System.out.println("   User: " + user);
        System.out.println("=========================================");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database connected!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✅ Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}