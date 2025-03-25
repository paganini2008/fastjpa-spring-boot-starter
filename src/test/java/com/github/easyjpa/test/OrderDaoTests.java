package com.github.easyjpa.test;

import java.time.LocalDate;
import java.util.List;
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

/**
 * 
 * @Description: OrderDaoTests
 * @Author: Fred Feng
 * @Date: 19/03/2025
 * @Version 1.0.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest(showSql = true)
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
        JpaQuery<Order, Tuple> jpaQuery = orderDao.customQuery();
        JpaSubQuery<User, Long> jpaSubQuery = jpaQuery.subQuery(User.class, "u", Long.class)
                .filter(Restrictions.eq(User::getId, Order::getUser)).select(Fields.toLong(1L));
        jpaQuery.filter(Restrictions.exists(jpaSubQuery)).distinct()
                .select(new ColumnList(Order::getUser)).list().forEach(m -> {
                    log.info(m.toString());
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Microwave oven", "Coffee maker"})
    public void test2(String itemName) {
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
                });
    }

    @Test
    public void test3() {
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
                        .addFields(Fields.avg(Order::getTotalPrice)))
                .setTransformer(Transformers.asCaseInsensitiveMap()).paginate(PageRequest.of(5))
                .forEachPage(eachPage -> {
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                    });
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Petter", "Jack"})
    public void test4(String username) {
        orderDao.customQuery().join(Order::getUser, "u", null)
                .filter(Restrictions.eq(User::getUsername, username))
                .sort(JpaSort.desc(Order::getOrderDate))
                .select(new ColumnList().addFields(Fields.root()).addTableAlias("u")).list()
                .forEach(t -> {
                    Order order = (Order) t.get(0);
                    User user = (User) t.get(1);
                    log.info("Order: " + order + ", User: " + user);
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
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                    });
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
                    log.info(String.format(
                            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s ======================",
                            eachPage.getPageNumber(), eachPage.getTotalPages(),
                            eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                    });
                });
    }

    @Test
    public void test7() {
        userDao.customPage().leftJoin(User::getOrders, "o", null)
                .join(Order::getOrderProducts, "op", null).join(OrderProduct::getProduct, "p", null)
                .filter(Restrictions.notNull(Product::getDiscount)
                        .and(Restrictions.in(Product::getId,
                                productDao.query().subQuery(Stock.class, "s", Long.class)
                                        .filter(Restrictions.gt(Stock::getAmount, 100L))
                                        .select(Stock::getProductId))))
                .groupBy(new FieldList(Product::getName, Product::getLocation))
                .sort(JpaSort.desc(4), JpaSort.desc(5))
                .select(new ColumnList().addColumns(Product::getName, Product::getLocation)
                        .addColumns(Fields.count(Product::getId).as("productAmount"),
                                Fields.count(Order::getId).as("orderAmount"),
                                Fields.sum(OrderProduct::getAmount).as("totalAmount"), Fields
                                        .abs(Fields.minus(
                                                Fields.multiply(
                                                        Fields.multiply(Product::getPrice,
                                                                Product::getDiscount),
                                                        Fields.sum(OrderProduct::getAmount)),
                                                Fields.multiply(Product::getPrice,
                                                        Fields.sum(OrderProduct::getAmount))))
                                        .as("savings")))
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
    }

    @AfterAll
    public void end() {
        log.info("=========== OrderDaoTests End. ===========");
    }

}
