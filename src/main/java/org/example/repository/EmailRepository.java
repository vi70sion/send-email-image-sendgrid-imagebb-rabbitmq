package org.example.repository;

import org.example.model.EmailForm;

import java.sql.*;
import java.time.LocalDateTime;

import static org.example.utility.Constants.*;

public class EmailRepository {

    private static Connection _connection;

    public EmailRepository() {
    }

    public boolean addEmail(EmailForm emailForm) {
        try {
            sqlConnection();
            String sql = "INSERT INTO emails (recipient, content) VALUES (?,?)";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, emailForm.getRecipient());
            statement.setString(2, emailForm.getContent());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false;    //duplicate entry
            } else {
                return false;    //other errors
            }
        }
    }

    public int addEmailGetId(EmailForm emailForm) {
        try {
            sqlConnection();
            String sql = "INSERT INTO emails (recipient, content) VALUES (?,?)";
            PreparedStatement statement = _connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, emailForm.getRecipient());
            statement.setString(2, emailForm.getContent());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                return  (resultSet.next()) ? resultSet.getInt(1) : 0;
            }
        } catch (SQLException e) {
            return 0;    //errors
        } finally {
            return 0;
        }
    }

    public EmailForm getOneEmail() {
        try {
            sqlConnection();
            String sql = "SELECT * FROM emails WHERE sent IS NULL ORDER BY id ASC LIMIT 1";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            boolean hasResults = resultSet.next();
            if(!hasResults) return null;
            return new EmailForm(resultSet.getInt("id"),
                                 resultSet.getString("recipient"),
                                 resultSet.getString("content"),
                                null);
        } catch (SQLException e) {
            // sql error
        }
        return null;
    }

    public boolean updateEmail(EmailForm emailForm) {
        try {
            sqlConnection();
            String sql = "UPDATE emails SET sent = ? WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(2, emailForm.getId());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            //
        }
        return false;
    }


    public static void sqlConnection() {
        try {
            _connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // connection issues
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // handle any other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
