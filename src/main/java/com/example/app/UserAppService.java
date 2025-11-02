package com.example.app;

import java.util.List;
import java.util.Optional;

import com.example.dao.UserDao;
import com.example.entity.User;

public class UserAppService {
    private final UserDao userApp; 

    public UserAppService(UserDao userApp) {
        this.userApp = userApp;
    }

    public User createUser(String name, String email, Integer age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (age == null || age < 0) {
            throw new IllegalArgumentException("Age must be non-negative");
        }

        User user = new User(name, email, age);
        userApp.create(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        return userApp.findById(id);
    }

    public List<User> findAll() {
        return userApp.findAll();
    }

    public User updateUser(Long id, String name, String email, Integer age) {
        Optional<User> existingOpt = findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        User user = existingOpt.get();
        if (name != null && !name.trim().isEmpty()) user.setName(name);
        if (email != null && !email.trim().isEmpty()) user.setEmail(email);
        if (age != null && age >= 0) user.setAge(age);

        userApp.update(user);
        return user;
    }

    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        userApp.deleteById(id);
    }
}