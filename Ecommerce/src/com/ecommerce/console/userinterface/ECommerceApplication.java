package com.ecommerce.console.userinterface;

import java.util.List;
import java.util.Scanner;

import com.ecommerce.database.CartDAOImpl;
import com.ecommerce.database.ProductDAOImpl;
import com.ecommerce.database.UserDAOImpl;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

public class ECommerceApplication {

    public static void main(String[] args) {
        UserDAOImpl userDAO = new UserDAOImpl();
        ProductDAOImpl productDAO = new ProductDAOImpl();
        CartDAOImpl cartDAO = new CartDAOImpl();
        Scanner sc = new Scanner(System.in);
        int userId = -1; // Placeholder for logged-in user's ID
        
        while (true) {
            System.out.println("Welcome to E-Commerce Application");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. View Products");
            System.out.println("4. Buy Products / Add To Cart");
            System.out.println("5. Exit");
            System.out.println("Enter your choice:");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (choice == 1) {
                // Register User
                User user = new User();
                System.out.println("Enter Username: ");
                user.setUsername(sc.nextLine());
                System.out.println("Enter Password: ");
                user.setPassword(sc.nextLine());
                System.out.println("Enter First Name: ");
                user.setFirstName(sc.nextLine());
                System.out.println("Enter Last Name: ");
                user.setLastName(sc.nextLine());
                System.out.println("Enter Email: ");
                user.setEmail(sc.nextLine());
                System.out.println("Enter Mobile: ");
                user.setMobile(sc.nextLine());
                System.out.println("Enter City: ");
                user.setCity(sc.nextLine());
                user.setRole("user");

                userDAO.registerUser(user);
                System.out.println("Would you like to log in now? (yes/no)");
                String autoLogin = sc.nextLine();
                if ("yes".equalsIgnoreCase(autoLogin)) {
                    if (userDAO.loginUser(user.getUsername(), user.getPassword())) {
                        System.out.println("Login successful! Welcome, " + user.getFirstName() + "!");
                        userId = userDAO.getUserIdByUsername(user.getUsername());
                    } else {
                        System.out.println("Login failed. Try again.");
                    }
                }
            } else if (choice == 2) {
                // Login
                System.out.println("Enter Username: ");
                String username = sc.nextLine();
                System.out.println("Enter Password: ");
                String password = sc.nextLine();

                if (userDAO.loginUser(username, password)) {
                    System.out.println("Login successful!");
                    userId = userDAO.getUserIdByUsername(username);
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            } else if (choice == 3) {
                // View Products
                if (userId == -1) {
                    System.out.println("You need to log in first.");
                    continue;
                }
                System.out.println("Available Products:");
                List<Product> products = productDAO.getAllProducts();
                if (products.isEmpty()) {
                    System.out.println("No products available at the moment.");
                } else {
                    for (Product product : products) {
                        System.out.println(product);
                    }
                }
            } else if (choice == 4) {
                // Add to Cart
                if (userId == -1) {
                    System.out.println("You need to log in first.");
                    continue;
                }
                System.out.println("Enter Product ID: ");
                int productId = sc.nextInt();
                System.out.println("Enter Quantity: ");
                int quantity = sc.nextInt();

                if (cartDAO.addToCart(userId, productId, quantity)) {
                    System.out.println("Product added to cart successfully!");
                } else {
                    System.out.println("Failed to add product to cart.");
                }
            } else if (choice == 5) {
                // View Cart
                if (userId == -1) {
                    System.out.println("You need to log in first.");
                    continue;
                }
                List<Cart> cartItems = cartDAO.getCartItems(userId);
                if (cartItems.isEmpty()) {
                    System.out.println("Your cart is empty.");
                } else {
                    System.out.println("Your Cart Items:");
                    for (Cart cart : cartItems) {
                        System.out.println("Product ID: " + cart.getProductId() + ", Quantity: " + cart.getQuantity());
                    }
                }
            } else if (choice == 6) {
                // Exit
                System.out.println("Thank you for visiting! Goodbye.");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
        sc.close();
    }
}
