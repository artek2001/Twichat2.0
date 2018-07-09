package com.artek;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseMessages {

    public static String mainOwner;
    public static String fromUser;

    public String gettingMessages(String mainOwner, String fromUser) {
        String url = "jdbc:mysql://localhost:3306/twichat";
        String username = "root";
        String password = "root";

        System.out.println("Connecting database...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            this.mainOwner = mainOwner;
            this.fromUser = fromUser;

            Statement stmt = connection.createStatement() ;
            String query = String.format("SELECT * FROM users WHERE owner = '%s' AND users = '%s'", mainOwner, fromUser) ;

            ResultSet rs = stmt.executeQuery(query) ;

            while (rs.next()) {
                return rs.getString("messages");
            }
            System.out.println("Database connected!");

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "NO messages from this user";
    }

    public void updateDatabaseWithMessages(String messages, String owner, String user) {
        String url = "jdbc:mysql://localhost:3306/twichat";
        String username = "root";
        String password = "root";

        System.out.println("Connecting database...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            Statement stmt = connection.createStatement() ;
            String query = String.format("UPDATE users SET messages = '%s' WHERE owner = '%s' AND users = '%s'", messages, owner, user) ;
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.executeUpdate();

            System.out.println("Database connected!");

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Messages updated");
    }

    public boolean isUserPresentInDatabase(String user, String owner) {
        String url = "jdbc:mysql://localhost:3306/twichat";
        String username = "root";
        String password = "root";

        System.out.println("Connecting database...");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            this.mainOwner = mainOwner;
            this.fromUser = fromUser;

            Statement stmt = connection.createStatement() ;
            String query = String.format("SELECT * FROM users WHERE owner = '%s' AND users = '%s'", owner, user) ;

            ResultSet rs = stmt.executeQuery(query) ;
            while (rs.next()) {
                System.out.println("PRESENT MESSAGES ARE " + rs.getString("messages"));
                return true;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
