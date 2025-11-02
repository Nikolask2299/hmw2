package com.example;

import java.util.Scanner;

import com.example.app.UserAppService;
import com.example.dao.UserDaoImpl;
import com.example.entity.User;

public class App {
    private static final UserAppService userData = new UserAppService(new UserDaoImpl());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        for (;;) {
            System.out.println("1. Create user");
            System.out.println("2. Find user by id");
            System.out.println("3. Find all users");
            System.out.println("4. Update user");
            System.out.println("5. Delete user by id");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
        
            var choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } 

            switch (choice) {
                case 1 -> createUser();
                case 2 -> findUserById();
                case 3 -> listAllUsers();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createUser() {
        
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter age: ");
        Integer age = Integer.valueOf(scanner.nextLine());

        User user = new User(name, email, age);
        try {
            userData.createUser(name, email, age);
            System.out.println("User created: " + user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void findUserById() {
        System.out.print("Enter user ID: ");
        Long id = Long.valueOf(scanner.nextLine());
        userData.findById(id).ifPresentOrElse(
                user -> System.out.println("Found: " + user),
                () -> System.out.println("User not found.")
        );
    }

    private static void listAllUsers() {
        var users = userData.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Enter user ID to update: ");
        Long id = Long.valueOf(scanner.nextLine());
        var existingUserOpt = userData.findById(id).get();
        
        System.out.print("Enter new name (current: " + existingUserOpt.getName() + "): ");
        String name = scanner.nextLine();

        System.out.print("Enter new email (current: " + existingUserOpt.getEmail() + "): ");
        String email = scanner.nextLine();

        System.out.print("Enter new age (current: " + existingUserOpt.getAge() + "): ");
        Integer age = Integer.valueOf(scanner.nextLine());

        try {
            userData.updateUser(id, name, email, age);
            System.out.println("User updated: " + existingUserOpt);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        Long id = Long.valueOf(scanner.nextLine());
        try {
            userData.deleteUser(id);
            System.out.println("User deleted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}