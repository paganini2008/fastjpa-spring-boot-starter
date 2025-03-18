package com.github.fastjpa.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import com.github.fastjpa.example.dao.OrderDao;
import com.github.fastjpa.example.dao.OrderProductDao;
import com.github.fastjpa.example.dao.ProductDao;
import com.github.fastjpa.example.dao.UserDao;
import com.github.fastjpa.example.entity.User;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JpaTests {

    private static Logger log = LoggerFactory.getLogger(JpaTests.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

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
    public void testTotalCount() {
        User user = userDao.query().eq(User::getUsername, "Jack").eq(User::getPassword, "123456")
                .selectThis().first();
        log.info("Load user: {}", user);
        assertEquals(user, new User("Jack", "123456", "Jack001@jpatest.com"));

    }

}
