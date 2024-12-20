package com.ecommerce.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.interfaces.CartDAOInterface;
import com.ecommerce.model.Cart;

public class CartDAOImpl implements CartDAOInterface {

    @Override
    public boolean addToCart(int userId, int productId, int quantity) {
        try (Connection conn = DataBaseConnection.connect()) {
            // Check if product exists in the Products table
            String checkProductQuery = "SELECT product_id FROM Products WHERE product_id = ?";
            PreparedStatement checkProductStmt = conn.prepareStatement(checkProductQuery);
            checkProductStmt.setInt(1, productId);
            ResultSet rs = checkProductStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Product with ID " + productId + " does not exist.");
                return false; // Product doesn't exist, return false
            }

            // Check stock availability in Products table
            String stockQuery = "SELECT quantity FROM Products WHERE product_id = ?";
            PreparedStatement stockStmt = conn.prepareStatement(stockQuery);
            stockStmt.setInt(1, productId);
            ResultSet stockRs = stockStmt.executeQuery();

            if (stockRs.next()) {
                int availableStock = stockRs.getInt("quantity");

                if (quantity > availableStock) {
                    System.out.println("Insufficient stock available.");
                    return false; // Not enough stock, return false
                }

                // Check if product already exists in the user's cart
                String checkCartQuery = "SELECT * FROM Cart WHERE user_id = ? AND prod_id = ?";
                PreparedStatement checkCartStmt = conn.prepareStatement(checkCartQuery);
                checkCartStmt.setInt(1, userId);
                checkCartStmt.setInt(2, productId);
                ResultSet cartCheck = checkCartStmt.executeQuery();

                if (cartCheck.next()) {
                    // If product is already in the cart, update the quantity
                    String updateCartQuery = "UPDATE Cart SET quantity = quantity + ? WHERE user_id = ? AND prod_id = ?";
                    PreparedStatement updateCartStmt = conn.prepareStatement(updateCartQuery);
                    updateCartStmt.setInt(1, quantity);
                    updateCartStmt.setInt(2, userId);
                    updateCartStmt.setInt(3, productId);
                    updateCartStmt.executeUpdate();
                } else {
                    // If product is not in the cart, insert it
                    String cartQuery = "INSERT INTO Cart (user_id, prod_id, quantity) VALUES (?, ?, ?)";
                    PreparedStatement cartStmt = conn.prepareStatement(cartQuery);
                    cartStmt.setInt(1, userId);
                    cartStmt.setInt(2, productId);
                    cartStmt.setInt(3, quantity);
                    cartStmt.executeUpdate();
                }

                // Update product stock
                String updateStockQuery = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ?";
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
                updateStockStmt.setInt(1, quantity);
                updateStockStmt.setInt(2, productId);
                updateStockStmt.executeUpdate();

                System.out.println("Product added to cart successfully!");
                return true; // Product successfully added to cart
            } else {
                System.out.println("Product not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Cart> getCartItems(int userId) {
        List<Cart> cartItems = new ArrayList<>();
        try (Connection conn = DataBaseConnection.connect()) {
            String query = "SELECT * FROM Cart WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setProductId(rs.getInt("prod_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cartItems.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }


	public List<Cart> getPurchaseHistory(int userId) {
    List<Cart> purchaseHistory = new ArrayList<>();
    try (Connection conn = DataBaseConnection.connect()) {
        String query = "SELECT prod_id, quantity FROM PurchaseHistory WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Cart cart = new Cart();
            cart.setProductId(rs.getInt("prod_id"));
            cart.setQuantity(rs.getInt("quantity"));
            purchaseHistory.add(cart);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return purchaseHistory;
}


	public double calculateUserBill(int userId) {
    double totalBill = 0.0;
    try (Connection conn = DataBaseConnection.connect()) {
        String query = "SELECT c.prod_id, c.quantity, p.price FROM Cart c INNER JOIN Products p ON c.prod_id = p.product_id WHERE c.user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int quantity = rs.getInt("quantity");
            double price = rs.getDouble("price");
            totalBill += (quantity * price);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return -1; // Indicates failure
    }
    return totalBill;
}


}
