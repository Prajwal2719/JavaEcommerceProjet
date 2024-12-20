package com.ecommerce.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.ecommerce.interfaces.PurchaseHistoryDAOInterface;

public class PurchaseHistoryDAOImpl implements PurchaseHistoryDAOInterface {

    @Override
    public boolean purchaseItems(int userId) {
    	
    	    try (Connection conn = DataBaseConnection.connect()) {
    	        String cartQuery = "SELECT cart_id, prod_id, quantity FROM Cart WHERE user_id = ?";
    	        PreparedStatement cartStmt = conn.prepareStatement(cartQuery);
    	        cartStmt.setInt(1, userId);
    	        ResultSet cartRs = cartStmt.executeQuery();

    	        boolean purchaseSuccessful = false;
    	        boolean allItemsProcessed = true; // Tracks if all items were successfully processed.

    	        while (cartRs.next()) {
    	            int cartId = cartRs.getInt("cart_id");
    	            int productId = cartRs.getInt("prod_id");
    	            int quantity = cartRs.getInt("quantity");

    	            // Fetch product details (price and stock)
    	            String productQuery = "SELECT price, stock FROM Products WHERE prod_id = ?";
    	            PreparedStatement productStmt = conn.prepareStatement(productQuery);
    	            productStmt.setInt(1, productId);
    	            ResultSet productRs = productStmt.executeQuery();

    	            if (!productRs.next()) {
    	                System.out.println("Product ID " + productId + " does not exist. Skipping item.");
    	                allItemsProcessed = false;
    	                continue;
    	            }

    	            double price = productRs.getDouble("price");
    	            int stock = productRs.getInt("stock");

    	            if (quantity > stock) {
    	                System.out.println("Insufficient stock for Product ID " + productId + ". Skipping item.");
    	                allItemsProcessed = false;
    	                continue;
    	            }

    	            double totalPrice = price * quantity;

    	            // Add to PurchaseHistory
    	            String purchaseQuery = "INSERT INTO PurchaseHistory (user_id, prod_id, quantity, total_price) VALUES (?, ?, ?, ?)";
    	            PreparedStatement purchaseStmt = conn.prepareStatement(purchaseQuery);
    	            purchaseStmt.setInt(1, userId);
    	            purchaseStmt.setInt(2, productId);
    	            purchaseStmt.setInt(3, quantity);
    	            purchaseStmt.setDouble(4, totalPrice);
    	            int rowsInserted = purchaseStmt.executeUpdate();

    	            if (rowsInserted > 0) {
    	                purchaseSuccessful = true;

    	                // Deduct stock
    	                String updateStockQuery = "UPDATE Products SET stock = stock - ? WHERE prod_id = ?";
    	                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
    	                updateStockStmt.setInt(1, quantity);
    	                updateStockStmt.setInt(2, productId);
    	                updateStockStmt.executeUpdate();

    	                // Remove item from Cart
    	                String deleteCartQuery = "DELETE FROM Cart WHERE cart_id = ?";
    	                PreparedStatement deleteCartStmt = conn.prepareStatement(deleteCartQuery);
    	                deleteCartStmt.setInt(1, cartId);
    	                deleteCartStmt.executeUpdate();
    	            }
    	        }

    	        if (purchaseSuccessful) {
    	            System.out.println("Purchase completed successfully!");
    	        } else if (!allItemsProcessed) {
    	            System.out.println("Some items could not be processed due to insufficient stock.");
    	        } else {
    	            System.out.println("No items purchased. Please check your cart.");
    	        }

    	        return purchaseSuccessful;
    	    } catch (Exception e) {
    	        System.err.println("Error during purchase: " + e.getMessage());
    	        e.printStackTrace();
    	    }
    	    return false;
    	}

    @Override
    public double calculateTotalBill(int userId) {
        double totalBill = 0.0;
        try (Connection conn = DataBaseConnection.connect()) {
            // Calculate the total price of items currently in the cart
            String query = "SELECT SUM(c.quantity * p.price) AS total_bill FROM Cart c INNER JOIN Products p ON c.prod_id = p.prod_id WHERE c.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalBill = rs.getDouble("total_bill");
            }
        } catch (Exception e) {
            System.err.println("Error calculating total bill: " + e.getMessage());
            e.printStackTrace();
        }
        return totalBill;
    }
}
