package org.example.repository;

import org.example.model.Image;
import java.sql.*;
import static org.example.utility.Constants.*;

public class ImgRepository {
    private static Connection _connection;

    public ImgRepository() {
    }

    public boolean addImg(Image image) {
        try {
            sqlConnection();
            String sql = "INSERT INTO images (img) VALUES (?)";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBytes(1, image.getImage());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            return false;    //errors
        }
    }

    public Image getOneImage() {
        try {
            sqlConnection();
            String sql = "SELECT * FROM images WHERE url IS NULL ORDER BY id ASC LIMIT 1";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            boolean hasResults = resultSet.next();
            if(!hasResults) return null;
            return new Image(resultSet.getInt("id"),
                    resultSet.getBytes("img"),
                    null);
        } catch (SQLException e) {
            // sql error
        }
        return null;
    }

    public boolean updateImage(Image image) {
        try {
            sqlConnection();
            String sql = "UPDATE images SET img = ?, url = ? WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBytes(1, null);
            statement.setString(2, image.getImageUrl());
            statement.setInt(3, image.getId());
            return (statement.executeUpdate() > 0) ? true : false;
        } catch (SQLException e) {
            //sql error
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