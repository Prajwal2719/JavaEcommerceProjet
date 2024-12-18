package com.ecommerce.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ecommerce.interfaces.UserDAOInterface;
import com.ecommerce.model.User;

public class UserDAOImpl implements UserDAOInterface{
	Connection conn=null;
	PreparedStatement stmt=null;
	@Override
	public void registerUser(User user) {
        try {
        	conn = DataBaseConnection.connect();
            String query = "INSERT INTO UsersDetails (username, password, first_name, last_name, email, mobile, city, role) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getMobile());
            stmt.setString(7, user.getCity());
            stmt.setString(8, user.getRole());
            stmt.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	try {
        		if (stmt != null) stmt.close();
                if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }

	@Override
	public boolean loginUser(String username, String password) {
		try (Connection conn = DataBaseConnection.connect()) {
	        String query = "SELECT * FROM UsersDetails WHERE username = ? AND password = ?";
	        PreparedStatement stmt = conn.prepareStatement(query);
	        stmt.setString(1, username);
	        stmt.setString(2, password);

	        return stmt.executeQuery().next(); // If a row exists, credentials are valid
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public int getUserIdByUsername(String username) {
        int userId = -1;
        try (Connection conn = DataBaseConnection.connect()) {
            String query = "SELECT user_id FROM UsersDetails WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

}
