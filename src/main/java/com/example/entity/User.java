package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User() {}

    public User(String name, String email, Integer age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (age == null || age < 0) {
            if (age == null) {
                throw new IllegalArgumentException("Age cannot be null");
            } else {
                throw new IllegalArgumentException("Age cannot be negative");
            }
        }

        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        if (id == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            return id;
        }
    }

    public void setId(Long id) {
        if (id == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            this.id = id;
        }
    }

    public String getName() {
        if (name == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            return name;
        }
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        } else {
            this.name = name;
        }
    }

    public String getEmail() {
        if (email == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            return email;
        }
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        } else {
            this.email = email;
        }
    }
   
   public Integer getAge() {
        if (age == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            return age;
        }
   }

   public void setAge(Integer age) {
        if (age == null || age < 0) {
            if (age == null) {
                throw new IllegalArgumentException("Age cannot be null");
            } else {
                throw new IllegalArgumentException("Age cannot be negative");
            }
        } else {
            this.age = age;
        }
   }

   public LocalDateTime getCreatedAt() {
        if (createdAt == null) {
            throw new IllegalStateException("User has not been persisted yet");
        } else {
            return createdAt;
        }
   }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", created_at=" + createdAt +
                '}';
    }
}
