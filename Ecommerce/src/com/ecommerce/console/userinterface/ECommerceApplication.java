package com.ecommerce.console.userinterface;

import java.util.List;
import java.util.Scanner;


import com.ecommerce.database.UserDAOImpl;

import com.ecommerce.model.User;

public class ECommerceApplication {

	public static void main(String[] args) {
		UserDAOImpl userDAO= new UserDAOImpl();
        System.out.println("Welcome to E-Commerce Application");
        
        System.out.println("1. Register User");
        System.out.println("2. Login");
        System.out.println("3. View Products");
        
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline
        
        if (choice == 1) {
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
        }else if (choice == 2) {
            System.out.println("Enter Username: ");
            String username = sc.nextLine();
            System.out.println("Enter Password: ");
            String password = sc.nextLine();

            if (userDAO.loginUser(username, password)) {
                System.out.println("Login successful!");
                // Navigate to user operations here
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }
}

