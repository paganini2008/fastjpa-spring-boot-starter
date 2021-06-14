package com.github.easyjpa.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import com.github.easyjpa.ColumnList;
import com.github.easyjpa.FilterList;
import com.github.easyjpa.test.config.JpaConfig;
import com.github.easyjpa.test.dao.UserDao;
import com.github.easyjpa.test.entity.User;
import com.github.easyjpa.test.service.UserService;

/**
 * 
 * @Description: UserDaoTests
 * @Author: Fred Feng
 * @Date: 22/03/2025
 * @Version 1.0.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest(showSql = true)
@ContextConfiguration(classes = {JpaConfig.class, UserService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTests {

    private static final Logger log = LoggerFactory.getLogger(UserDaoTests.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @BeforeAll
    public void begin() {
        log.info("=========== UserDaoTests Begin. ===========");
        userService.saveRandomUsers();
    }

    @Test
    public void testSelectAll() {
        userDao.query().selectThis().list().forEach(u -> {
            log.info(u.toString());
        });
    }

    @Test
    public void testGetUserByUsernameAndPassword() {
        User user = userDao.query().filter(
                new FilterList().eq(User::getUsername, "Jack").eq(User::getPassword, "123456"))
                .selectThis().one();
        log.info("Load user: {}", user);
        assertTrue(user != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"scott003", "lee004"})
    public void testGetUserByEmail(String email) {
        User user = userDao.query().filter(new FilterList().like(User::getEmail, email))
                .select(new ColumnList(User::getUsername, User::getPassword)).first();
        log.info("Load user: {}", user);
        assertTrue(user != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc009"})
    public void testGetUserNotFoundByEmail(String email) {
        User user = userDao.query().filter(new FilterList().like(User::getEmail, email))
                .select(new ColumnList(User::getUsername, User::getPassword, User::getEmail))
                .first();
        log.info("Load user: {}", user);
        assertTrue(user == null);
    }

    @AfterAll
    public void end() {
        log.info("=========== UserDaoTests End. ===========");
    }

}
