package com.github.fastjpa.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import com.github.fastjpa.ColumnList;
import com.github.fastjpa.FieldList;
import com.github.fastjpa.Fields;
import com.github.fastjpa.Function;
import com.github.fastjpa.IfExpression;
import com.github.fastjpa.JpaSort;
import com.github.fastjpa.LambdaFilter;
import com.github.fastjpa.Restrictions;
import com.github.fastjpa.Transformers;
import com.github.fastjpa.example.dao.ProductDao;
import com.github.fastjpa.example.dao.StockDao;
import com.github.fastjpa.example.entity.Product;
import com.github.fastjpa.example.entity.Stock;
import com.github.fastjpa.page.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: ProductDaoTests
 * @Author: Fred Feng
 * @Date: 19/03/2025
 * @Version 1.0.0
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = JpaConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTests {

    private static final Logger log = LoggerFactory.getLogger(ProductDaoTests.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private StockDao stockDao;

    @BeforeAll
    @Commit
    public void begin() {
        List<Product> list = List.of(
                new Product("Juicer", BigDecimal.valueOf(80), BigDecimal.valueOf(0.9),
                        LocalDate.of(2025, 1, 10), "Indonesia"),
                new Product("Electric fan", BigDecimal.valueOf(30), null, LocalDate.of(2025, 2, 10),
                        "Singapore"),
                new Product("Washing machine", BigDecimal.valueOf(120), BigDecimal.valueOf(0.9),
                        LocalDate.of(2025, 3, 10), "China"),
                new Product("Coffee maker", BigDecimal.valueOf(150), null,
                        LocalDate.of(2025, 2, 27), "Australia"),
                new Product("Hair dryer", BigDecimal.valueOf(110), null, LocalDate.of(2024, 12, 10),
                        "Japan"),
                new Product("Shaver", BigDecimal.valueOf(60), BigDecimal.valueOf(0.85),
                        LocalDate.of(2024, 12, 27), "Australia"),
                new Product("Rice cooker", BigDecimal.valueOf(220), BigDecimal.valueOf(0.9),
                        LocalDate.of(2025, 1, 27), "Australia"),
                new Product("Microwave oven", BigDecimal.valueOf(320), BigDecimal.valueOf(0.9),
                        LocalDate.of(2025, 1, 5), "New Zealand"),
                new Product("Iron", BigDecimal.valueOf(25), null, LocalDate.of(2024, 12, 27),
                        "China"),
                new Product("Flashlight", BigDecimal.valueOf(10), null, LocalDate.of(2025, 3, 5),
                        "Vietnam"),
                new Product("Electric heater", BigDecimal.valueOf(256), BigDecimal.valueOf(0.8),
                        LocalDate.of(2024, 12, 20), "Thailand"),
                new Product("Water dispenser", BigDecimal.valueOf(101), BigDecimal.valueOf(0.85),
                        LocalDate.of(2024, 12, 5), "Thailand"));
        list = productDao.saveAll(list);
        log.info("Total products: {}", productDao.count());

        List<Stock> stocks = new ArrayList<>();
        for (Product product : list) {
            Stock stock = new Stock();
            stock.setAmount(99999L);
            stock.setProductId(product.getId());
            stocks.add(stock);
        }
        stockDao.saveAll(stocks);
        assertTrue(list.size() == productDao.count() && stocks.size() == stockDao.count());
    }

    @Order(1)
    @Test
    public void test1() {
        productDao.query(ProductVo.class)
                .filter(new LambdaFilter().gte(Product::getPrice, BigDecimal.valueOf(100)).and()
                        .notNull(Product::getDiscount))
                .select(new ColumnList(Product::getName, Product::getPrice).addColumns(
                        Fields.multiply(Product::getPrice, Product::getDiscount).as("actualPrice")))
                .list().forEach(vo -> {
                    System.out.println(vo);
                });
    }

    @Order(2)
    @Test
    public void test2() {
        productDao.query(ProductVo.class)
                .filter(new LambdaFilter().gte(Product::getPrice, BigDecimal.valueOf(200))
                        .and(() -> new LambdaFilter().eq(Product::getLocation, "Australia").or()
                                .eq(Product::getLocation, "Thailand")))
                .sort(JpaSort.desc(Fields.avg(Product::getPrice)))
                .select(new ColumnList(Product::getName, Product::getLocation, Product::getPrice)
                        .addColumns(Fields.multiply(Product::getPrice, Product::getDiscount)
                                .as("actualPrice")))
                .list().forEach(vo -> {
                    System.out.println(vo);
                });
    }

    @Order(3)
    @Test
    public void test3() {
        productDao.customQuery().groupBy(Product::getLocation)
                .sort(JpaSort.desc(Fields.avg(Product::getPrice)))
                .select(new ColumnList(Product::getLocation).addColumns(
                        Fields.max(Product::getPrice).as("maxPrice"),
                        Fields.min(Product::getPrice).as("minPrice"),
                        Fields.avg(Product::getPrice).as("avgPrice"), Fields.count(1).as("amount")))
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    System.out.println(vo);
                });
    }

    @Order(4)
    @Test
    public void test4() {
        productDao.customQuery().groupBy(new FieldList(Product::getLocation))
                .having(Restrictions.gt(Fields.avg(Product::getPrice), 50d)).orderBy(4, false)
                .select(new ColumnList(Product::getLocation).addColumns(
                        Fields.max(Product::getPrice).as("maxPrice"),
                        Fields.min(Product::getPrice).as("minPrice"),
                        Fields.avg(Product::getPrice).as("avgPrice"), Fields.count(1).as("amount")))
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    System.out.println(vo);
                });
    }

    @Order(5)
    @Test
    public void test5() {
        ColumnList columnList = new ColumnList();
        columnList
                .addColumns(Fields.concat(Fields.concat(Fields.max("price", String.class), "/"),
                        Fields.min("price", String.class)).as("repr"))
                .addColumns(Product::getLocation);
        productDao.customQuery().groupBy("location").select(columnList)
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    System.out.println(vo);
                });
    }

    @Order(6)
    @Test
    public void test6() {
        ColumnList columnList = new ColumnList().addColumns(
                Function.build("LOWER", String.class, Product::getName).as("name"),
                Function.build("UPPER", String.class, Product::getLocation).as("location"));
        productDao.customQuery().select(columnList).list(10).forEach(t -> {
            System.out.println(t.toString());
        });
    }

    @Order(7)
    @Test
    public void test7() {
        IfExpression<String, String> ifExpression =
                new IfExpression<String, String>(Product::getLocation);
        ifExpression = ifExpression.when("Indonesia", "Asia").when("Japan", "Asia")
                .when("China", "Asia").when("Singapore", "Asia").when("Vietnam", "Asia")
                .when("Thailand", "Asia").when("Australia", "Oceania")
                .when("New Zealand", "Oceania").otherwise("Other");
        ColumnList columnList = new ColumnList().addColumns(ifExpression.as("area"))
                .addColumns(Product::getLocation);
        productDao.customQuery().select(columnList).list().forEach(t -> {
            System.out.println(t.toString());
        });
    }


    @Order(8)
    @Test
    public void test8() {
        productDao.customPage().crossJoin(Stock.class, "a")
                .filter(new LambdaFilter().eq(Stock::getProductId, Product::getId))
                .select(new ColumnList().addColumns(Product::getId, Product::getName)
                        .addColumns(Stock::getAmount))
                .setTransformer(Transformers.asBean(ProductStockVo.class))
                .paginate(PageRequest.of(10)).forEachPage(eachPage -> {
                    System.out.println(String.format(
                            "====================== Pagination: %s/%s ======================",
                            eachPage.getPageNumber(), eachPage.getTotalPages()));
                    eachPage.getContent().forEach(vo -> {
                        System.out.println(vo.toString());
                    });
                    System.out.println();
                });
    }

    @AfterAll
    @Commit
    public void end() {
        // productDao.deleteAll();
        // stockDao.deleteAll();
    }

    @Getter
    @Setter
    @ToString
    public static class ProductVo {

        private String name;
        private String location;
        private BigDecimal price;
        private BigDecimal actualPrice;

    }

    @Getter
    @Setter
    @ToString
    public static class ProductStockVo {
        private Long id;
        private String name;
        private Long amount;
    }

    @Getter
    @Setter
    @ToString
    public static class ProductAggregationVo {

        private String location;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private BigDecimal avgPrice;
        private Long amount;
        private String repr;
    }

    @Getter
    @Setter
    @ToString
    public static class AreaVo {

        private String area;
        private Long count;
    }

}
