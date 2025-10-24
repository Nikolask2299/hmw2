package com.example.app;

import com.example.app.UserApp;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


public class DataApp implements UserApp {
    private static final Logger logger = LoggerFactory.getLogger(DataApp.class);

    @Override
    public void create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created successfully with id: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (e instanceof org.hibernate.exception.ConstraintViolationException) {
                logger.error("User with email {} already exists", user.getEmail());
            } else {
                logger.error("Error creating user: {}", e.getMessage());
                throw new RuntimeException("Failed to create user", e);
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            logger.error("Invalid user ID: {}", id);
            return Optional.empty();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (e instanceof org.hibernate.exception.ConstraintViolationException) {
                logger.error("User with ID {} not found", id);
            } else {
                logger.error("Error finding user by ID: {}", e.getMessage());
                throw new RuntimeException("Failed to find user by ID", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            if (e instanceof org.hibernate.exception.ConstraintViolationException) {
                logger.error("No users found");
            } else {
                logger.error("Error finding all users: {}", e.getMessage());
                throw new RuntimeException("Failed to find all users", e);
            }
        }
        
        return List.of();
    }

    @Override
    public User update(User user) {
        if (user == null) {
            logger.error("Invalid user: {}", user);
            return null;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("User updated successfully: {}", user);
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } 
            logger.error("Error updating user: {}", e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            logger.error("Invalid user ID: {}", id);
        } 

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
            logger.info("User deleted successfully with ID: {}", id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user with ID: {}", id, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}


