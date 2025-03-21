package com.github.fastjpa.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import com.github.fastjpa.ColumnList;
import com.github.fastjpa.example.dao.OrderDao;
import com.github.fastjpa.example.dao.OrderProductDao;
import com.github.fastjpa.example.dao.UserDao;
import com.github.fastjpa.example.entity.User;


@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTests {

    private static final Logger log = LoggerFactory.getLogger(UserDaoTests.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;



    @Autowired
    private OrderProductDao orderProductDao;

    @BeforeAll
    public void init() {
        List.of(new User("Jack", "123456", "Jack001@jpatest.com"),
                new User("Petter", "123456", "Petter002@jpatest.com"),
                new User("Scott", "123456", "scott003@jpatest.com"),
                new User("Lee", "123456", "lee004@jpatest.com"),
                new User("Terry", "123456", "terry005@jpatest.com")).forEach(user -> {
                    userDao.save(user);
                });
        log.info("Total users: {}", userDao.count());
    }

    @Test
    public void testGetUserByUsernameAndPassword() {
        User user = userDao.query().eq(User::getUsername, "Jack").eq(User::getPassword, "123456")
                .selectThis().first();
        log.info("Load user: {}", user);
        assertTrue(user != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"scott003", "lee004"})
    public void testGetUserByEmail(String email) {
        User user = userDao.query().like(User::getEmail, email)
                .select(new ColumnList(User::getUsername, User::getPassword)).first();
        log.info("Load user: {}", user);
        assertTrue(user != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc009"})
    public void testGetUserNotFoundByEmail(String email) {
        User user = userDao.query().like(User::getEmail, email)
                .select(new ColumnList(User::getUsername, User::getPassword)).first();
        log.info("Load user: {}", user);
        assertTrue(user == null);
    }

    @AfterAll
    public void clean() {
        // userDao.delete().execute();
    }

}
