package com.github.fastjpa.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import com.github.fastjpa.example.dao.OrderDao;
import com.github.fastjpa.example.dao.OrderProductDao;
import com.github.fastjpa.example.dao.ProductDao;
import com.github.fastjpa.example.dao.UserDao;
import com.github.fastjpa.example.entity.Product;
import com.github.fastjpa.example.entity.User;

/**
 * 
 * @Description: OrderDaoTests
 * @Author: Fred Feng
 * @Date: 19/03/2025
 * @Version 1.0.0
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {JpaConfig.class, UserOrderService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDaoTests {

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private ProductDao productDao;

    @BeforeAll
    @Commit
    public void begin() {
        List<User> users = userDao.findAll();
        List<Product> products = productDao.findAll();
        for (User user : users) {
            for (int i = 0, n = ThreadLocalRandom.current().nextInt(1, 10); i < n; i++) {
                userOrderService.makeOrder(user.getId(), randomShoppingCart(products));
            }
        }
    }

    private Map<String, Integer> randomShoppingCart(List<Product> products) {
        Map<String, Integer> items = new HashMap<String, Integer>();
        for (int i = 0, n = ThreadLocalRandom.current().nextInt(1, 100); i < n; i++) {
            int index = ThreadLocalRandom.current().nextInt(products.size());
            Product product = products.get(index);
            if (items.containsKey(product.getName())) {
                int amount = items.get(product.getName());
                amount += ThreadLocalRandom.current().nextInt(1, 20);
                items.put(product.getName(), amount);
            } else {
                items.put(product.getName(), ThreadLocalRandom.current().nextInt(1, 20));
            }
        }
        return items;
    }

    @Order(1)
    @Test
    public void test1() {
        // PageResponse<Tuple> pageResponse = orderDao.paginateForTuple().l.leftJoin("product", "p")
        // .filter(Restrictions.gte("p", "price", 50)).sort(JpaSort.desc("createTime"))
        // .selectAlias("p").list(PageRequest.of(10));
    }

    @AfterAll
    @Commit
    public void end() {
        // orderProductDao.deleteAll();
        // orderDao.deleteAll();
    }

}
