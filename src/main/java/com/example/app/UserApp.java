package com.example.app;

import com.example.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserApp {
    void create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    User update(User user);
    void deleteById(Long id);
}