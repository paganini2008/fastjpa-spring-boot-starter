package com.github.easyjpa.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import com.github.easyjpa.Fields;
import com.github.easyjpa.FilterList;
import com.github.easyjpa.Function;
import com.github.easyjpa.IfExpression;
import com.github.easyjpa.JpaSort;
import com.github.easyjpa.JpaSubQuery;
import com.github.easyjpa.Restrictions;
import com.github.easyjpa.Transformers;
import com.github.easyjpa.page.PageRequest;
import com.github.easyjpa.test.config.JpaConfig;
import com.github.easyjpa.test.dao.ProductDao;
import com.github.easyjpa.test.dao.StockDao;
import com.github.easyjpa.test.entity.Product;
import com.github.easyjpa.test.entity.Stock;
import com.github.easyjpa.test.service.ProductService;
import jakarta.persistence.Tuple;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = {JpaConfig.class, ProductService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTests {

    private static final Logger log = LoggerFactory.getLogger(ProductDaoTests.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private StockDao stockDao;

    @BeforeAll
    public void begin() {
        log.info("=========== ProductDaoTests Begin. ===========");
        productService.saveRandomProducts();
    }

    @Test
    public void test1() {
        productDao.query().selectThis().list().forEach(p -> {
            log.info(p.toString());
        });
    }

    @Test
    public void test2() {
        List<ProductVo> dataList = new ArrayList<>();
        productDao.query(ProductVo.class)
                .filter(new FilterList().gte(Product::getPrice, BigDecimal.valueOf(100)).and()
                        .notNull(Product::getDiscount))
                .select(new ColumnList(Product::getName, Product::getPrice).addColumns(
                        Fields.multiply(Product::getPrice, Product::getDiscount).as("actualPrice")))
                .list().forEach(vo -> {
                    log.info(vo.toString());
                    dataList.add(vo);
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            if (vo.getPrice().compareTo(BigDecimal.valueOf(100)) >= 0
                    && vo.getActualPrice().compareTo(vo.getPrice()) < 0) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @Test
    public void test3() {
        List<ProductVo> dataList = new ArrayList<>();
        productDao.query(ProductVo.class)
                .filter(new FilterList().gte(Product::getPrice, BigDecimal.valueOf(200))
                        .and(() -> new FilterList().eq(Product::getLocation, "Australia").or()
                                .eq(Product::getLocation, "Thailand")))
                .sort(JpaSort.desc(Fields.toInteger(4)))
                .select(new ColumnList(Product::getName, Product::getLocation, Product::getPrice)
                        .addColumns(Fields.multiply(Product::getPrice, Product::getDiscount)
                                .as("actualPrice")))
                .list().forEach(vo -> {
                    log.info(vo.toString());
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            if (vo.getPrice().compareTo(BigDecimal.valueOf(200)) >= 0
                    && ("Australia".equals(vo.getLocation())
                            || "Thailand".equals(vo.getLocation()))) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @Test
    public void test4() {
        List<ProductAggregationVo> dataList = new ArrayList<>();
        productDao.customQuery().groupBy(Product::getLocation)
                .sort(JpaSort.desc(Fields.avg(Product::getPrice)))
                .select(new ColumnList(Product::getLocation).addColumns(
                        Fields.max(Product::getPrice).as("maxPrice"),
                        Fields.min(Product::getPrice).as("minPrice"),
                        Fields.avg(Product::getPrice).as("avgPrice"), Fields.count(1).as("amount")))
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    log.info(vo.toString());
                    dataList.add(vo);
                });
        int locationSize = productDao.query().distinct()
                .select(new ColumnList(Product::getLocation)).list().size();
        assertTrue(locationSize == dataList.size());
    }

    @Test
    public void test5() {
        List<ProductAggregationVo> dataList = new ArrayList<>();
        productDao.customQuery().groupBy(Product::getLocation)
                .having(Restrictions.gt(Fields.avg(Product::getPrice), 50d)).sort(JpaSort.desc(4))
                .select(new ColumnList(Product::getLocation).addColumns(
                        Fields.max(Product::getPrice).as("maxPrice"),
                        Fields.min(Product::getPrice).as("minPrice"),
                        Fields.avg(Product::getPrice).as("avgPrice"), Fields.count(1).as("amount")))
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    log.info(vo.toString());
                    dataList.add(vo);
                });
        AtomicInteger counter = new AtomicInteger();
        dataList.forEach(vo -> {
            if (vo.getAvgPrice().doubleValue() > 50d) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @Test
    public void test6() {
        List<ProductAggregationVo> dataList = new ArrayList<>();
        ColumnList columnList = new ColumnList();
        columnList
                .addColumns(Fields.concat(Fields.concat(Fields.max("price", String.class), "/"),
                        Fields.min("price", String.class)).as("repr"))
                .addColumns(Product::getLocation);
        productDao.customQuery().groupBy(Product::getLocation).select(columnList)
                .setTransformer(Transformers.asBean(ProductAggregationVo.class)).list()
                .forEach(vo -> {
                    log.info(vo.toString());
                    dataList.add(vo);
                });
        int locationSize = productDao.query().distinct()
                .select(new ColumnList(Product::getLocation)).list().size();
        assertTrue(locationSize == dataList.size());
    }

    @Test
    public void test7() {
        List<Tuple> dataList = new ArrayList<>();
        ColumnList columnList = new ColumnList().addColumns(
                Function.build("LOWER", String.class, Product::getName).as("name"),
                Function.build("UPPER", String.class, Product::getLocation).as("location"));
        productDao.customQuery().select(columnList).list(10).forEach(t -> {
            log.info(t.toString());
            dataList.add(t);
        });
        assertTrue(dataList.size() <= 10);
    }

    @Test
    public void test8() {
        List<Tuple> dataList = new ArrayList<>();
        IfExpression<String, String> ifExpression =
                new IfExpression<String, String>(Product::getLocation).when("Indonesia", "Asia")
                        .when("Japan", "Asia").when("China", "Asia").when("Singapore", "Asia")
                        .when("Vietnam", "Asia").when("Thailand", "Asia")
                        .when("Australia", "Oceania").when("New Zealand", "Oceania")
                        .otherwise("Other");
        ColumnList columnList = new ColumnList().addColumns(ifExpression.as("area"))
                .addColumns(Product::getLocation);
        productDao.customQuery().select(columnList).list().forEach(t -> {
            log.info(t.toString());
            dataList.add(t);
        });
        assertTrue(dataList.stream().map(t -> t.get("area")).distinct().count() == 2);
    }

    @Test
    public void test9() {
        List<String> locations = List.of("Australia", "New Zealand", "Thailand");
        List<ProductStockVo> dataList = new ArrayList<>();
        productDao.customPage().crossJoin(Stock.class, "a")
                .filter(new FilterList().eq(Stock::getProductId, Product::getId).and()
                        .in(Product::getLocation, locations))
                .select(new ColumnList()
                        .addColumns(Product::getId, Product::getName, Product::getLocation)
                        .addColumns(Stock::getAmount))
                .setTransformer(Transformers.asBean(ProductStockVo.class))
                .paginate(PageRequest.of(10)).forEachPage(eachPage -> {
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
            if (locations.contains(vo.getLocation())) {
                counter.incrementAndGet();
            }
        });
        assertTrue(counter.get() == dataList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Australia", "New Zealand"})
    public void test10(String location) {
        JpaSubQuery<Product, Long> subQuery = stockDao.update().subQuery(Product.class, Long.class)
                .filter(Restrictions.eq(Product::getLocation, location)).select(Product::getId);
        long rows = stockDao.count(Restrictions.in(Stock::getProductId, subQuery));

        int updatedRows = stockDao.update()
                .setField(Stock::getAmount, Fields.plusValue(Stock::getAmount, 1000))
                .filter(Restrictions.in(Stock::getProductId, subQuery)).execute();
        log.info("Affected rows: {}", updatedRows);
        assertTrue(rows == updatedRows);
    }

    @Test
    public void test11() {
        Long productId = stockDao.query(Long.class).sort(JpaSort.desc(Stock::getAmount))
                .select(new ColumnList(Stock::getProductId)).first();
        int expectedRows = productId != null && productId > 0 ? 1 : 0;
        int affectedRows = productDao.update()
                .set(Product::getPrice, BigDecimal.valueOf(1000), Product::getDiscount,
                        BigDecimal.valueOf(0.8f), Product::getProduceDate, LocalDate.now())
                .filter(Restrictions.eq(Product::getId, productId)).execute();
        log.info("Affected rows: {}", affectedRows);
        assertTrue(affectedRows == expectedRows);
    }

    @AfterAll
    public void end() {
        log.info("=========== ProductDaoTests End. ===========");
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
        private String location;
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

}
