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
            String userRole = "guest"; // Default role is "guest"

            while (true) {
            	System.out.println("\nWelcome to E-Commerce Application\n");
                if ("guest".equals(userRole)) {
                	
                    System.out.println("Welcome, Guest!");
                    System.out.println("1. View Products");
                    System.out.println("2. Register");
                    System.out.println("3. Login");
                    System.out.println("4. Exit");
                    System.out.println("Enter your choice:");

                    int choice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    if (choice == 1) {
                        // View Products
                        System.out.println("Available Products:");
                        List<Product> products = productDAO.getAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("No products available at the moment.");
                        } else {
                            for (Product product : products) {
                                System.out.println(product);
                            }
                        }
                    } else if (choice == 2) {
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
                        System.out.println("User registered successfully!");
                    } else if (choice == 3) {
                        // Login
                        System.out.println("Enter Username: ");
                        String username = sc.nextLine();
                        System.out.println("Enter Password: ");
                        String password = sc.nextLine();

                        if ("admin".equals(username) && "admin".equals(password)) {
                            System.out.println("Admin login successful!");
                            userId = 0; // Placeholder for admin ID
                            userRole = "admin";
                        } else if (userDAO.loginUser(username, password)) {
                            System.out.println("Login successful!");
                            userId = userDAO.getUserIdByUsername(username);
                            userRole = "user";
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    } else if (choice == 4) {
                        // Exit
                        System.out.println("Thank you for visiting! Goodbye.");
                        break;
                    } else {
                        System.out.println("Invalid choice, please try again.");
                    }
                } else if ("user".equals(userRole)) {
                    System.out.println("User Menu:");
                    System.out.println("1. View Products");
                    System.out.println("2. Buy Products / Add To Cart");
                    System.out.println("3. View Cart");
                    System.out.println("4. Purchase Items");
                    System.out.println("5. Logout");
                    System.out.println("Enter your choice:");

                    int choice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    if (choice == 1) {
                        // View Products
                        System.out.println("Available Products:");
                        List<Product> products = productDAO.getAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("No products available at the moment.");
                        } else {
                            for (Product product : products) {
                                System.out.println(product);
                            }
                        }
                    } else if (choice == 2) {
                        // Add to Cart
                        System.out.println("Enter Product ID: ");
                        int productId = sc.nextInt();
                        System.out.println("Enter Quantity: ");
                        int quantity = sc.nextInt();

                        if (cartDAO.addToCart(userId, productId, quantity)) {
                            System.out.println("Product added to cart successfully!");
                        } else {
                            System.out.println("Failed to add product to cart.");
                        }
                    } else if (choice == 3) {
                        // View Cart
                        List<Cart> cartItems = cartDAO.getCartItems(userId);
                        if (cartItems.isEmpty()) {
                            System.out.println("Your cart is empty.");
                        } else {
                            System.out.println("Your Cart Items:");
                            for (Cart cart : cartItems) {
                                System.out.println("Product ID: " + cart.getProductId() + ", Quantity: " + cart.getQuantity());
                            }
                        }
                    }else if (choice == 5) {
                        // Logout
                        System.out.println("Logged out successfully.");
                        userId = -1;
                        userRole = "guest";
                    } else {
                        System.out.println("Invalid choice, please try again.");
                    }
                } else if ("admin".equals(userRole)) {
                    System.out.println("Admin Menu:");
                    System.out.println("1. Add Product");
                   
                    System.out.println("2. Calculate and Display User Bill");
                    System.out.println("6. Logout");
                    System.out.println("Enter your choice:");

                    int choice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    if (choice == 1) {
                        // Add Product
                        System.out.println("Enter Product Name: ");
                        String name = sc.nextLine();
                        System.out.println("Enter Product Description: ");
                        String description = sc.nextLine();
                        System.out.println("Enter Product Price: ");
                        double price = sc.nextDouble();
                        System.out.println("Enter Product Quantity: ");
                        int quantity = sc.nextInt();

                        if (productDAO.addProduct(name, description, price, quantity)) {
                            System.out.println("Product added successfully!");
                        } else {
                            System.out.println("Failed to add product.");
                        }
                    } else if (choice == 2) {
                        // Calculate and Display User Bill
                        System.out.println("Enter User ID to calculate the bill: ");
                        int userIdToBill = sc.nextInt();
                        double totalBill = cartDAO.calculateUserBill(userIdToBill);
                        if (totalBill >= 0) {
                            System.out.println("Total bill for User ID " + userIdToBill + ": $" + totalBill);
                        } else {
                            System.out.println("Failed to calculate bill. Please check the user's cart.");
                        }
                    } else if (choice == 6) {
                        // Logout
                        System.out.println("Logged out successfully.");
                        userId = -1;
                        userRole = "guest";
                    } else {
                        System.out.println("Invalid choice, please try again.");
                    }
                }
            }
        }
    }

            
 
