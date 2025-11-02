package com.example.dao;

import java.util.List;
import java.util.Optional;

import com.example.entity.User;

public interface UserDao {
    void create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    User update(User user);
    void deleteById(Long id);
}