package com.github.fastjpa.example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import com.github.fastjpa.ColumnList;
import com.github.fastjpa.FieldList;
import com.github.fastjpa.Fields;
import com.github.fastjpa.JpaSort;
import com.github.fastjpa.Restrictions;
import com.github.fastjpa.Transformers;
import com.github.fastjpa.example.dao.OrderDao;
import com.github.fastjpa.example.dao.OrderProductDao;
import com.github.fastjpa.example.dao.ProductDao;
import com.github.fastjpa.example.dao.UserDao;
import com.github.fastjpa.example.entity.Order;
import com.github.fastjpa.example.entity.OrderProduct;
import com.github.fastjpa.example.entity.Product;
import com.github.fastjpa.example.entity.User;
import com.github.fastjpa.page.PageRequest;

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

    public void test1() {

    }

    @Test
    public void test3() {
        orderDao.customPage().join(Order::getUser, "u", null)
                .filter(Restrictions.between(Order::getOrderDate,
                        LocalDate.of(2025, 2, 1).atStartOfDay(),
                        LocalDate.of(2025, 2, 28).atStartOfDay()))
                .groupBy(new FieldList().addField(Order::getOrderDate).addField(User::getUsername))
                .having(Restrictions.gt(Fields.avg(Order::getTotalPrice), 20000D))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addColumns(User::getUsername)
                        .addColumns(Order::getOrderDate)
                        .addFields(Fields.avg(Order::getTotalPrice)))
                .setTransformer(Transformers.asCaseInsensitiveMap()).paginate(PageRequest.of(5))
                .forEachPage(eachPage -> {
                    System.out.println(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        System.out.println(vo.toString());
                    });
                    System.out.println();
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Petter", "Jack"})
    public void test4(String username) {
        orderDao.customQuery().join(Order::getUser, "u", null)
                .filter(Restrictions.eq("u", "username", username))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addFields(Fields.root()).addTableAlias("u")).list()
                .forEach(t -> {
                    Order order = (Order) t.get(0);
                    User user = (User) t.get(1);
                    System.out.println("Order: " + order + ", User: " + user);
                });
    }

    @Test
    public void test5() {
        orderDao.customPage().leftJoin(Order::getOrderProducts, "op", null)
                .leftJoin(OrderProduct::getProduct, "p", null)
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addFields(Fields.root()).addTableAlias("p"))
                .setTransformer(Transformers.asMap()).paginate(PageRequest.of(10))
                .forEachPage(eachPage -> {
                    System.out.println(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        System.out.println(vo.toString());
                    });
                    System.out.println();
                });
    }

    @Test
    public void test6() {
        orderDao.customPage().rightJoin(Order::getOrderProducts, "op", null)
                .rightJoin(OrderProduct::getProduct, "p", null)
                .sort(JpaSort.desc(Order::getOrderDate), JpaSort.desc(OrderProduct::getAmount))
                .select(new ColumnList()
                        .addColumns(Order::getId, Order::getTotalPrice, Order::getOrderDate)
                        .addColumns(OrderProduct::getAmount)
                        .addColumns(Product::getName, Product::getLocation))
                .setTransformer(Transformers.asMap()).paginate(PageRequest.of(10))
                .forEachPage(eachPage -> {
                    System.out.println(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s ======================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        System.out.println(vo.toString());
                    });
                    System.out.println();
                });
    }

    @AfterAll
    @Commit
    public void end() {
        orderProductDao.deleteAll();
        orderDao.deleteAll();
    }

}
