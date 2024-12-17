package com.ecommerce.console.userinterface;

import java.util.Scanner;

import com.ecommerce.database.UserDAOImpl;
import com.ecommerce.model.User;

public class ECommerceApplication {

	public static void main(String[] args) {
		UserDAOImpl userDAO= new UserDAOImpl();
        System.out.println("Welcome to E-Commerce Application");
        
        System.out.println("1. Register User");
        System.out.println("2. Exit");
        
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
            user.setRole("User");

            userDAO.registerUser(user);
        }
    }
}

