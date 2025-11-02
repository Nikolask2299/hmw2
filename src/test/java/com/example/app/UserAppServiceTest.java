package com.example.app;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dao.UserDao;
import com.example.entity.User;

@ExtendWith(MockitoExtension.class)
public class UserAppServiceTest {
    @Mock
    private UserDao userDao;

    private UserAppService userAppService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        userAppService = new UserAppService(userDao);
    }

    @Test
    void shouldCreateUserValidData() {
        String name = "John";
        String email = "john@example.com";
        Integer age = 25;

        userAppService.createUser(name, email, age);

        verify(userDao).create(argThat(
            user -> user.getName().equals(name) &&
                    user.getEmail().equals(email) &&
                    user.getAge().equals(age)
        ));
    }
    

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
            String name = "";
            String email = "john@example.com";
            Integer age = 25;

            assertThatThrownBy(() -> userAppService.createUser(name, email, age))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Name is required");

            verify(userDao, never()).create(any());
    }

    @Test
    void shouldFindById() {
        User user = new User("John", "john@example.com", 25);
        
        when(userAppService.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userAppService.findById(1L);

        assertThat(result).contains(user);
    }

    @Test
    void shouldThrowExceptionForInvalidId() {
        assertThatThrownBy(() -> userAppService.findById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID must be positive");
       
        verify(userDao, never()).findById(any());
    }

    @Test
    void shouldUpdateUser() {
        User existing = new User("Old", "old@example.com", 20);
        when(userDao.findById(1L)).thenReturn(Optional.of(existing));
        
        when(userDao.update(any())).thenAnswer(inv -> inv.getArgument(0));

        User updated = userAppService.updateUser(1L, "New", "new@example.com", 21);
        
        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getEmail()).isEqualTo("new@example.com");
        assertThat(updated.getAge()).isEqualTo(21);
        
        verify(userDao).update(existing);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userAppService.updateUser(999L, "X", "x@example.com", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with id: 999");
    }
}
