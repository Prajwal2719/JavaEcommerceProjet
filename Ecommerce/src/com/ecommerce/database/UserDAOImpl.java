package com.ecommerce.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            String query = "INSERT INTO UserDetails (username, password, first_name, last_name, email, mobile, city, role) " +
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
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    }

	@Override
	public boolean loginUser(String username, String password) {
        // Implementation for login (to be done later)
		return false;
	}

}
