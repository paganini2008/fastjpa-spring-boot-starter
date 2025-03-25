package com.github.easyjpa.test;

import static com.github.easyjpa.Fields.abs;
import static com.github.easyjpa.Fields.count;
import static com.github.easyjpa.Fields.minus;
import static com.github.easyjpa.Fields.multiply;
import static com.github.easyjpa.Fields.sum;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import com.github.easyjpa.ColumnList;
import com.github.easyjpa.FieldList;
import com.github.easyjpa.Fields;
import com.github.easyjpa.FilterList;
import com.github.easyjpa.JpaQuery;
import com.github.easyjpa.JpaSort;
import com.github.easyjpa.JpaSubQuery;
import com.github.easyjpa.Restrictions;
import com.github.easyjpa.Transformers;
import com.github.easyjpa.page.PageRequest;
import com.github.easyjpa.test.config.JpaConfig;
import com.github.easyjpa.test.dao.OrderDao;
import com.github.easyjpa.test.dao.OrderProductDao;
import com.github.easyjpa.test.dao.ProductDao;
import com.github.easyjpa.test.dao.UserDao;
import com.github.easyjpa.test.entity.Order;
import com.github.easyjpa.test.entity.OrderProduct;
import com.github.easyjpa.test.entity.Product;
import com.github.easyjpa.test.entity.Stock;
import com.github.easyjpa.test.entity.User;
import com.github.easyjpa.test.service.ProductService;
import com.github.easyjpa.test.service.UserOrderService;
import com.github.easyjpa.test.service.UserService;
import jakarta.persistence.Tuple;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: OrderDaoTests
 * @Author: Fred Feng
 * @Date: 19/03/2025
 * @Version 1.0.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = {JpaConfig.class, UserService.class, ProductService.class,
        UserOrderService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDaoTests {

    private static final Logger log = LoggerFactory.getLogger(OrderDaoTests.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductDao productDao;

    @BeforeAll
    public void begin() {
        log.info("=========== OrderDaoTests Begin. ===========");
        userService.saveRandomUsers();
        productService.saveRandomProducts();
        userOrderService.makeRandomOrders();
    }

    @Test
    public void test1() {
        final AtomicInteger counter = new AtomicInteger();
        JpaQuery<Order, Tuple> jpaQuery = orderDao.customQuery();
        JpaSubQuery<User, Long> jpaSubQuery = jpaQuery.subQuery(User.class, "u", Long.class)
                .filter(Restrictions.eq(User::getId, Order::getUser)).select(Fields.toLong(1L));
        jpaQuery.filter(Restrictions.exists(jpaSubQuery)).distinct()
                .select(new ColumnList(Order::getUser)).list().forEach(m -> {
                    log.info(m.toString());
                    counter.incrementAndGet();
                });
        assertTrue(counter.get() <= 5);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Microwave oven", "Coffee maker"})
    public void test2(String itemName) {
        final AtomicInteger counter = new AtomicInteger();
        JpaQuery<OrderProduct, Tuple> jpaQuery = orderProductDao.customQuery();
        JpaSubQuery<Product, Long> jpaSubQuery = jpaQuery.subQuery(Product.class, "p", Long.class)
                .filter(new FilterList().eq(Product::getId, OrderProduct::getProduct).and()
                        .eq(Product::getName, itemName))
                .select(Product::getId);
        jpaQuery.leftJoin(OrderProduct::getProduct, "p", null).join(Order.class, "o", null)
                .join(User.class, "u", null).filter(Restrictions.exists(jpaSubQuery))
                .select(new ColumnList(OrderProduct::getOrder, OrderProduct::getProduct,
                        OrderProduct::getAmount).addColumns(Product::getName)
                                .addColumns(User::getUsername))
                .setTransformer(Transformers.asMap()).list(10).forEach(m -> {
                    log.info(m.toString());
                    counter.incrementAndGet();
                });
        assertTrue(counter.get() <= 10);
    }

    @Test
    public void test3() {
        final List<Map<String, Object>> dataList = new ArrayList<>();
        orderDao.customPage().join(Order::getUser, "u", null)
                .filter(Restrictions.between(Order::getOrderDate,
                        LocalDate.of(2025, 2, 1).atStartOfDay(),
                        LocalDate.of(2025, 2, 28).atStartOfDay()))
                .groupBy(
                        new FieldList().addFields(Order::getOrderDate).addFields(User::getUsername))
                .having(Restrictions.gt(Fields.avg(Order::getTotalPrice), 20000D))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addColumns(User::getUsername)
                        .addColumns(Order::getOrderDate)
                        .addColumns(Fields.avg(Order::getTotalPrice).as("avgTotalPrice")))
                .setTransformer(Transformers.asCaseInsensitiveMap()).paginate(PageRequest.of(5))
                .forEachPage(eachPage -> {
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                        dataList.add(vo);
                    });
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            LocalDateTime ldt = (LocalDateTime) vo.get("orderDate");
            if (ldt.compareTo(LocalDate.of(2025, 2, 1).atStartOfDay()) >= 0
                    && ldt.compareTo(LocalDate.of(2025, 2, 28).atStartOfDay()) <= 0) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Petter", "Jack"})
    public void test4(String username) {
        List<User> userList = new ArrayList<>();
        orderDao.customQuery().join(Order::getUser, "u", null)
                .filter(Restrictions.eq(User::getUsername, username))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addFields(Fields.root()).addTableAlias("u")).list()
                .forEach(t -> {
                    Order order = (Order) t.get(0);
                    User user = (User) t.get(1);
                    log.info("Order: " + order + ", User: " + user);
                    userList.add(user);
                });
        AtomicInteger counter = new AtomicInteger();
        userList.forEach(u -> {
            if ("Petter".equals(u.getUsername()) || "Jack".equals(u.getUsername())) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == userList.size());
    }

    @Test
    public void test5() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        orderDao.customPage().leftJoin(Order::getOrderProducts, "op", null)
                .leftJoin(OrderProduct::getProduct, "p", null)
                .filter(Restrictions.eq(Product::getLocation, "Australia"))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addFields(Fields.root()).addTableAlias("p"))
                .setTransformer(Transformers.asMap()).paginate(PageRequest.of(10))
                .forEachPage(eachPage -> {
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                        dataList.add(vo);
                    });
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            String location = (String) vo.get("location");
            if ("Australia".equals(location)) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @Test
    public void test6() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        orderDao.customPage().rightJoin(Order::getOrderProducts, "op", null)
                .rightJoin(OrderProduct::getProduct, "p", null)
                .filter(Restrictions.eq(Product::getLocation, "Australia"))
                .sort(JpaSort.desc(Order::getOrderDate), JpaSort.desc(OrderProduct::getAmount))
                .select(new ColumnList()
                        .addColumns(Order::getId, Order::getTotalPrice, Order::getOrderDate)
                        .addColumns(OrderProduct::getAmount)
                        .addColumns(Product::getName, Product::getLocation))
                .setTransformer(Transformers.asMap()).paginate(PageRequest.of(10))
                .forEachPage(eachPage -> {
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s ======================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                    });
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            String location = (String) vo.get("location");
            if ("Australia".equals(location)) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @Test
    public void test7() {
        List<UserOrderVo> dataList = new ArrayList<>();
        userDao.customPage().leftJoin(User::getOrders, "o", null)
                .join(Order::getOrderProducts, "op", null).join(OrderProduct::getProduct, "p", null)
                .filter(Restrictions.notNull(Product::getDiscount)
                        .and(Restrictions.in(Product::getId,
                                productDao.query().subQuery(Stock.class, "s", Long.class)
                                        .filter(Restrictions.gt(Stock::getAmount, 10000L))
                                        .select(Stock::getProductId))))
                .groupBy(new FieldList(Product::getName, Product::getLocation))
                .sort(JpaSort.desc(4), JpaSort.desc(5))
                .select(new ColumnList().addColumns(Product::getName, Product::getLocation)
                        .addColumns(count(Product::getId).as("productAmount"),
                                count(Order::getId).as("orderAmount"),
                                sum(OrderProduct::getAmount).as("totalAmount"),
                                abs(minus(
                                        multiply(multiply(Product::getPrice, Product::getDiscount),
                                                sum(OrderProduct::getAmount)),
                                        multiply(Product::getPrice, sum(OrderProduct::getAmount))))
                                                .as("savings")))
                .setTransformer(Transformers.asBean(UserOrderVo.class)).paginate(PageRequest.of(10))
                .forEachPage(eachPage -> {
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s ======================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                        dataList.add(vo);
                    });
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            if (vo.getSavings().doubleValue() > 0) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @ParameterizedTest
    @CsvSource({"'Flashlight,Iron'"})
    public void test8(String str) {
        String[] itemNames = str.split(",");
        JpaSubQuery<OrderProduct, Order> subQuery =
                orderDao.query().subQuery(OrderProduct.class, "op", Order.class)
                        .join(OrderProduct::getProduct, "p", null)
                        .filter(new FilterList().eq(Product::getId, OrderProduct::getProduct)
                                .in(Product::getName, List.of(itemNames)))
                        .select(OrderProduct::getOrder);
        int rows = orderDao.delete().filter(Restrictions.exists(subQuery)).execute();
        log.info("Affected rows: {}", rows);
        assertTrue(rows >= 0);
    }

    @Getter
    @Setter
    @ToString
    private static class UserOrderVo {
        private String name;
        private String location;
        private Long productAmount;
        private Long orderAmount;
        private Integer totalAmount;
        private Number savings;

    }

    @AfterAll
    public void end() {
        log.info("=========== OrderDaoTests End. ===========");
    }

}
