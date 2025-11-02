package com.example.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.entity.User;



@Testcontainers
public class DataAppTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    private UserDaoImpl userDaoImpl;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() throws Exception {
        String url = postgreSQLContainer.getJdbcUrl();

        var config = new Configuration();
        config.setProperty("hibernate.connection.url", url)
            .setProperty("hibernate.connection.username", postgreSQLContainer.getUsername())
            .setProperty("hibernate.connection.password", postgreSQLContainer.getPassword())
            .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
            .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
            .setProperty("hibernate.hbm2ddl.auto", "create-drop")
            .setProperty("hibernate.show_sql", "false")
            .addAnnotatedClass(com.example.entity.User.class);
        
        sessionFactory = config.buildSessionFactory();
        setSessionFactoryInHibernateUtil(sessionFactory);
        userDaoImpl = new UserDaoImpl();
    }

    private void setSessionFactoryInHibernateUtil(SessionFactory sf) throws Exception {
        Field field = com.example.util.HibernateUtil.class.getDeclaredField("sessionFactory");
        field.setAccessible(true);
        Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        field.set(null, sf);
    }

    @AfterEach
    void tearDown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Test
    void shouldSaveAndFindUserById(){
        User user = new User("Alice", "alice@test.com", 30);

        userDaoImpl.create(user);

        Optional<User> foundUser = userDaoImpl.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get().getName().equals("Alice"));
        assertTrue(foundUser.get().getEmail().equals("alice@test.com"));
        assertTrue(foundUser.get().getAge() == 30);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound(){
        Optional<User> foundUser = userDaoImpl.findById(999L);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindAllUsers(){
        userDaoImpl.create(new User("User1", "u1@test.com", 20));
        userDaoImpl.create(new User("User2", "u2@test.com", 25));

        List<User> users = userDaoImpl.findAll();
        
       
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName).contains("User1", "User2");
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("Old", "old@test.com", 40);
        userDaoImpl.create(user);

        user.setName("New");
        user.setEmail("new@test.com");
        user.setAge(41);
        userDaoImpl.update(user);

        Optional<User> updated = userDaoImpl.findById(user.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("New");
        assertThat(updated.get().getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void shouldDeleteUser() {
        User user = new User("ToDelete", "del@test.com", 50);
        userDaoImpl.create(user);

        userDaoImpl.deleteById(user.getId());

        Optional<User> found = userDaoImpl.findById(user.getId());
        assertThat(found).isEmpty();
    } 
}
