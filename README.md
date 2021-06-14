

# EasyJPA – Your Best Partner for JPA Development!

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3+-brightgreen?style=for-the-badge&logo=springboot)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-yellow?style=for-the-badge&logo=hibernate)
![JPA](https://img.shields.io/badge/JPA-Jakarta%20Persistence%20API-blue?style=for-the-badge)
![EasyJPA](https://img.shields.io/badge/EasyJPA-Lightweight-blueviolet?style=for-the-badge)


### It's time to say goodbye to JPA Criteria API complexity! EasyJPA makes your code sleek, simple, and powerful!

**EasyJPA** elegantly streamlines JPA's Criteria API with a fully **Lambda-expression-based** and developer-friendly API, making dynamic queries both intuitive and efficient. It significantly reduces SQL/JPQL complexity, accelerates development, and improves code readability, ensuring a clean and concise query experience.

With comprehensive support for complex SQL queries, **EasyJPA** enables seamless execution of **multi-table joins (INNER, LEFT, RIGHT, CROSS JOINs), subqueries, aggregations, computed columns, and filtering operations**. Its **fluent API with full Lambda expression support** allows developers to construct queries programmatically, eliminating the need for raw SQL while retaining maximum flexibility.

## Features
---------------------------
* Dynamic Queries
* Computed and Function-Based Columns
* Grouping and Filtering
* Sorting
* List Queries and Pagination
* Complex Subqueries
* Inner Join, Left Join, Right Join, and Cross Join
* Update and Delete Operations
* Lambda Expression Support
* Hibernate Implementation by Default

## Examples
**1. Query All Users**

```java
@Autowired
private UserDao userDao;

@BeforeAll
public void saveRandomUsers() {
    List.of(new User("Jack", "123456", "Jack001@jpatest.com"),
            new User("Petter", "123456", "Petter002@jpatest.com"),
            new User("Scott", "123456", "scott003@jpatest.com"),
            new User("Lee", "123456", "lee004@jpatest.com"),
            new User("Terry", "123456", "terry005@jpatest.com")
           ).forEach(user -> {
                userDao.save(user);
           });
    log.info("Total users: {}", userDao.count());
}

/**
Hibernate: 
    select
        u1_0.id,
        u1_0.email,
        u1_0.password,
        u1_0.username 
    from
        example_user u1_0
**/
@Test
public void testSelectAll() {
    userDao.query()
           .selectThis()
           .list()
           .forEach(u -> {
                log.info(u.toString());
           });
}
```

**2. Basic Search Conditions**

``` java
/**
Hibernate: 
    select
        u1_0.id,
        u1_0.email,
        u1_0.password,
        u1_0.username 
    from
        example_user u1_0 
    where
        u1_0.username=? 
        and u1_0.password=?
**/
@Test
public void testGetUserByUsernameAndPassword() {
    User user = userDao.query().filter(new FilterList()
                                          .eq(User::getUsername, "Jack")
                                          .eq(User::getPassword, "123456")
                                       ).selectThis()
                                       .one();
    log.info("Load user: {}", user);
    assertTrue(user != null);
}

/**
Hibernate: 
    select
        u1_0.username,
        u1_0.password 
    from
        example_user u1_0 
    where
        u1_0.email like ? escape '' 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@ParameterizedTest
@ValueSource(strings = {"scott003", "lee004"})
public void testGetUserByEmail(String email) {
    User user = userDao.query()
                       .filter(new FilterList()
                                  .like(User::getEmail, email)
                       ).select(new ColumnList(
                                   User::getUsername, 
                                   User::getPassword)
                       ).first();
    log.info("Load user: {}", user);
    assertTrue(user != null);
    
}

/**
Hibernate: 
    select
        u1_0.username,
        u1_0.password 
    from
        example_user u1_0 
    where
        u1_0.email like ? escape '' 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@ParameterizedTest
@ValueSource(strings = {"abc009"})
public void testGetUserNotFoundByEmail(String email) {
    User user = userDao.query()
                       .filter(new FilterList()
                               .like(User::getEmail, email)
                       ).select(new ColumnList(
                                    User::getUsername, 
                                    User::getPassword, 
                                    User::getEmail)
                       ).first();
   log.info("Load user: {}", user);
   assertTrue(user == null);
}

```
**3. Nested Query Conditions & Sorting & Computed Columns**

``` java
@Autowired
private ProductDao productDao;

/**
Hibernate: 
    select
        p1_0.name,
        p1_0.location,
        p1_0.price,
        (p1_0.price*p1_0.discount) 
    from
        example_product p1_0 
    where
        p1_0.price>=? 
        and (
            p1_0.location=? 
            or p1_0.location=?
        ) 
    order by
        4 desc
 **/
@Test
public void test2() {
    productDao.query(ProductVo.class)
              .filter(new FilterList()
                          .gte(Product::getPrice, BigDecimal.valueOf(200))
                              .and(() -> new FilterList()
                                         .eq(Product::getLocation, "Australia")
                                             .or()
                                         .eq(Product::getLocation, "Thailand")
                               )
              ).sort(JpaSort.desc(Fields.toInteger(4))
              ).select(new ColumnList(
                            Product::getName, 
                            Product::getLocation, 
                            Product::getPrice
                       ).addColumns(
                            Fields.multiply(Product::getPrice, Product::getDiscount).as("actualPrice"))
                       ).list().forEach(vo -> {
                            log.info(vo.toString());
                       });
}
```

**4. Grouping & Aggregation & Filtering**

```java
/**
Hibernate: 
    select
        p1_0.location,
        max(p1_0.price),
        min(p1_0.price),
        avg(p1_0.price),
        count(1) 
    from
        example_product p1_0 
    group by
        p1_0.location 
    having
        avg(p1_0.price)>? 
    order by
        4 desc 
    offset
        ? rows
**/
@Test
public void test4() {
    productDao.customQuery()
              .groupBy(new FieldList(Product::getLocation))
              .having(Restrictions.gt(Fields.avg(Product::getPrice), 50d))
              .sort(JpaSort.desc(4))
              .select(new ColumnList(Product::getLocation).addColumns(
                   Fields.max(Product::getPrice).as("maxPrice"),
                   Fields.min(Product::getPrice).as("minPrice"),
                   Fields.avg(Product::getPrice).as("avgPrice"), 
                   Fields.count(1).as("amount"))
               ).setTransformer(Transformers.asBean(ProductAggregationVo.class))
               .list().forEach(vo -> {
                    log.info(vo.toString());
               });
}
```
**5. Using Function in Columns**

```java
/**
Hibernate: 
    select
        ((max(p1_0.price)||?)||min(p1_0.price)),
        p1_0.location 
    from
        example_product p1_0 
    group by
        p1_0.location 
    offset
        ? rows
**/
@Test
public void test5() {
    productDao.customQuery()
              .groupBy("location")
              .select(new ColumnList().addColumns(
                          Fields.concat(Fields.concat(Fields.max("price", String.class), "/"),
                                        Fields.min("price", String.class)).as("repr")
                          ).addColumns(Product::getLocation))
              .setTransformer(Transformers.asBean(ProductAggregationVo.class))
              .list().forEach(vo -> {
                  log.info(vo.toString());
              });
}

/**
Hibernate: 
    select
        lower(p1_0.name),
        upper(p1_0.location) 
    from
        example_product p1_0 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@Test
public void test6() {
    productDao.customQuery()
              .select(new ColumnList().addColumns(
                   Function.build("LOWER", String.class, Product::getName).as("name"),
                   Function.build("UPPER", String.class,Product::getLocation).as("location"))
              ).list(10).forEach(t -> {
                   log.info(t.toString());
              });
}

/**
Hibernate: 
    select
        case p1_0.location 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            when ? 
                then cast(? as varchar) 
            else cast(? as varchar) 
    end,
    p1_0.location 
from
    example_product p1_0
**/
@Test
public void test7() {
     IfExpression<String, String> ifExpression = new IfExpression<String, String>(Product::getLocation)
                .when("Indonesia", "Asia")
                .when("Japan", "Asia")
                .when("China", "Asia")
                .when("Singapore", "Asia")
                .when("Vietnam", "Asia")
                .when("Thailand", "Asia")
                .when("Australia", "Oceania")
                .when("New Zealand", "Oceania")
                .otherwise("Other");
     productDao.customQuery().select(new ColumnList()
                                        .addColumns(ifExpression.as("area"))
                                        .addColumns(Product::getLocation)
                                    ).list().forEach(t -> {
                                         log.info(t.toString());
                                    });
}
```
**6. Inner Join & Pagination**

```java
@Autowired
private OrderDao orderDao;

@Autowired
private OrderProductDao orderProductDao;

/**
Hibernate: 
    select
        o1_0.id,
        o1_0.order_date,
        o1_0.total_price,
        o1_0.user_id,
        u1_0.id,
        u1_0.email,
        u1_0.password,
        u1_0.username 
    from
        example_order o1_0 
    join
        example_user u1_0 
            on u1_0.id=o1_0.user_id 
    where
        u1_0.username=? 
    order by
        o1_0.order_date desc
**/
@ParameterizedTest
@ValueSource(strings = {"Petter", "Jack"})
public void test3(String username) {
    orderDao.customQuery().join(Order::getUser, "u", null)
                          .filter(Restrictions.eq(User::getUsername, username))
                          .sort(JpaSort.desc(Order::getOrderDate))
                          .select(new ColumnList()
                                  .addFields(Fields.root())
                                  .addTableAlias("u")
                          ).list().forEach(t -> {
                              Order order = (Order) t.get(0);
                              User user = (User) t.get(1);
                              log.info("Order: " + order + ", User: " + user);
                          });
}

/**
Hibernate: 
    select
        count(1) 
    from
        example_order o1_0 
    join
        example_user u1_0 
            on u1_0.id=o1_0.user_id 
    where
        o1_0.order_date between ? and ? 
    group by
        o1_0.order_date,
        u1_0.username 
    having
        avg(o1_0.total_price)>?
Hibernate: 
    select
        u1_0.username,
        o1_0.order_date,
        avg(o1_0.total_price) 
    from
        example_order o1_0 
    join
        example_user u1_0 
            on u1_0.id=o1_0.user_id 
    where
        o1_0.order_date between ? and ? 
    group by
        o1_0.order_date,
        u1_0.username 
    having
        avg(o1_0.total_price)>? 
    order by
        o1_0.order_date desc 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@Test
public void test4() {
    orderDao.customPage().join(Order::getUser, "u", null)
                         .filter(Restrictions.between(Order::getOrderDate,
                                      LocalDate.of(2025, 2, 1).atStartOfDay(),
                                      LocalDate.of(2025, 2, 28).atStartOfDay())
                          ).groupBy(new FieldList()
                                     .addFields(Order::getOrderDate)
                                     .addFields(User::getUsername)
                          ).having(Restrictions.gt(Fields.avg(Order::getTotalPrice), 20000D))
                         .sort(JpaSort.desc(Order::getOrderDate))
                         .select(new ColumnList()
                                     .addColumns(User::getUsername)
                                     .addColumns(Order::getOrderDate)
                                     .addFields(Fields.avg(Order::getTotalPrice))
                          ).setTransformer(Transformers.asCaseInsensitiveMap())
                         .paginate(PageRequest.of(5))
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

```

**7. Left Join & Pagination**

```java
/**
Hibernate: 
    select
        count(1) 
    from
        example_order o1_0 
    left join
        example_order_product op1_0 
            on o1_0.id=op1_0.order_id
Hibernate: 
    select
        o1_0.id,
        o1_0.order_date,
        o1_0.total_price,
        o1_0.user_id,
        p1_0.id,
        p1_0.discount,
        p1_0.location,
        p1_0.name,
        p1_0.price,
        p1_0.produce_date 
    from
        example_order o1_0 
    left join
        example_order_product op1_0 
            on o1_0.id=op1_0.order_id 
    left join
        example_product p1_0 
            on p1_0.id=op1_0.product_id 
    order by
        o1_0.order_date desc 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@Test
public void test5() {
    orderDao.customPage()
            .leftJoin(Order::getOrderProducts, "op", null)
            .leftJoin(OrderProduct::getProduct, "p", null)
            .sort(JpaSort.desc(Order::getOrderDate))
            .select(new ColumnList()
                    .addFields(Fields.root())
                    .addTableAlias("p")
            ).setTransformer(Transformers.asMap())
            .paginate(PageRequest.of(10))
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
```

**8. Right Join & Pagination**

```java
/**
Hibernate: 
    select
        count(1) 
    from
        example_order o1_0 
    right join
        example_order_product op1_0 
            on o1_0.id=op1_0.order_id 
    right join
        example_product p1_0 
            on p1_0.id=op1_0.product_id
Hibernate: 
    select
        o1_0.id,
        o1_0.total_price,
        o1_0.order_date,
        op1_0.amount,
        p1_0.name,
        p1_0.location 
    from
        example_order o1_0 
    right join
        example_order_product op1_0 
            on o1_0.id=op1_0.order_id 
    right join
        example_product p1_0 
            on p1_0.id=op1_0.product_id 
    order by
        o1_0.order_date desc,
        op1_0.amount desc 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@Test
public void test6() {
    orderDao.customPage()
            .rightJoin(Order::getOrderProducts, "op", null)
            .rightJoin(OrderProduct::getProduct, "p", null)
            .sort(JpaSort.desc(Order::getOrderDate), 
                  JpaSort.desc(OrderProduct::getAmount)
             ).select(new ColumnList()
                  .addColumns(Order::getId, 
                              Order::getTotalPrice, 
                              Order::getOrderDate)
                  .addColumns(OrderProduct::getAmount)
                  .addColumns(Product::getName, 
                              Product::getLocation)
             ).setTransformer(Transformers.asMap())
              .paginate(PageRequest.of(10))
              .forEachPage(eachPage -> {
                   log.info(String.format(
            "====================== PageNumber/TotalPage: %s/%s  Total Records: %s ======================",
                   eachPage.getPageNumber(), 
                   eachPage.getTotalPages(),
                   eachPage.getTotalRecords()));
                   eachPage.getContent().forEach(vo -> {
                       log.info(vo.toString());
                   });
             });
}
```

**9. Cross Join & Pagination**

``` java
/**
Hibernate: 
    select
        count(1) 
    from
        example_product p1_0,
        example_stock s1_0 
    where
        s1_0.product_id=p1_0.id
Hibernate: 
    select
        p1_0.id,
        p1_0.name,
        s1_0.amount 
    from
        example_product p1_0,
        example_stock s1_0 
    where
        s1_0.product_id=p1_0.id 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@Test
public void test8() {
    productDao.customPage()
              .crossJoin(Stock.class, "a")
              .filter(new FilterList()
                      .eq(Stock::getProductId, Product::getId)
              ).select(new ColumnList()
                       .addColumns(Product::getId, 
                                   Product::getName
                       ).addColumns(Stock::getAmount)
              ).setTransformer(Transformers.asBean(ProductStockVo.class))
               .paginate(PageRequest.of(10))
               .forEachPage(eachPage -> {
                    log.info(String.format(
              "====================== PageNumber/TotalPage: %s/%s  Total Records: %s =====================",
                    eachPage.getPageNumber(), 
                    eachPage.getTotalPages(),
                    eachPage.getTotalRecords()));
                    eachPage.getContent().forEach(vo -> {
                        log.info(vo.toString());
                    });
              });
}
```

**10. Subquery &  Join**

``` java
/**
Hibernate: 
    select
        distinct o1_0.user_id 
    from
        example_order o1_0 
    where
        exists(select
            1 
        from
            example_user u2_0 
        where
            u2_0.id=o1_0.user_id)
**/
@Test
public void test1() {
    JpaQuery<Order, Tuple> jpaQuery = orderDao.customQuery();
    JpaSubQuery<User, Long> jpaSubQuery = jpaQuery.subQuery(User.class, "u", Long.class)
                                                  .filter(Restrictions.eq(User::getId, Order::getUser))
                                                  .select(Fields.toLong(1L));
    jpaQuery.filter(Restrictions.exists(jpaSubQuery))
            .distinct()
            .select(new ColumnList(Order::getUser))
            .list().forEach(m -> {
                 log.info(m.toString());
            });
}

/**
Hibernate: 
    select
        op1_0.order_id,
        op1_0.product_id,
        op1_0.amount,
        p1_0.name,
        u1_0.username 
    from
        example_order_product op1_0 
    left join
        example_product p1_0 
            on p1_0.id=op1_0.product_id 
    join
        example_order o1_0 
            on o1_0.id=op1_0.order_id 
    join
        example_user u1_0 
            on u1_0.id=o1_0.user_id 
    where
        exists(select
            p3_0.id 
        from
            example_product p3_0 
        where
            p3_0.id=op1_0.product_id 
            and p3_0.name=?) 
    offset
        ? rows 
    fetch
        first ? rows only
**/
@ParameterizedTest
@ValueSource(strings = {"Microwave oven", "Coffee maker"})
public void test2(String itemName) {
     JpaQuery<OrderProduct, Tuple> jpaQuery = orderProductDao.customQuery();
     JpaSubQuery<Product, Long> jpaSubQuery = jpaQuery.subQuery(Product.class, "p", Long.class)
                                              .filter(new FilterList()
                                              .eq(Product::getId, OrderProduct::getProduct)
                                                  .and()
                                              .eq(Product::getName, itemName)
                                               ).select(Product::getId);
     jpaQuery.leftJoin(OrderProduct::getProduct, "p", null)
             .join(Order.class, "o", null)
             .join(User.class, "u", null)
             .filter(Restrictions.exists(jpaSubQuery))
             .select(new ColumnList(
                                    OrderProduct::getOrder, 
                                    OrderProduct::getProduct,
                                    OrderProduct::getAmount
                                   ).addColumns(Product::getName)
                                    .addColumns(User::getUsername)
             ).setTransformer(Transformers.asMap())
              .list(10)
              .forEach(m -> {
                  log.info(m.toString());
              });
}
```
**11. Update with Subquery**
```java
/**
Hibernate: 
    update
        example_stock s1_0 
    set
        amount=(s1_0.amount+cast(? as integer)) 
    where
        s1_0.product_id in ((select
            p1_0.id 
        from
            example_product p1_0 
        where
            p1_0.location=?))
**/
@ParameterizedTest
@ValueSource(strings = {"Australia", "New Zealand"})
public void test9(String location) {
    JpaSubQuery<Product, Long> subQuery = stockDao.update().subQuery(Product.class, Long.class)
                .filter(Restrictions.eq(Product::getLocation, location)).select(Product::getId);
    stockDao.update()
            .setField(Stock::getAmount, Fields.plusValue(Stock::getAmount, 1000))
            .filter(Restrictions.in(Stock::getProductId, subQuery))
            .execute();
}
```

**12. Delete with Subquery**
```java
/**
Hibernate: 
    delete 
    from
        example_order o1_0 
    where
        exists(select
            op1_0.order_id 
        from
            example_order_product op1_0 
        join
            example_product p1_0 
                on p1_0.id=op1_0.product_id 
        where
            p1_0.id=op1_0.product_id 
            and p1_0.name in (?, ?))
**/
@ParameterizedTest
@CsvSource({"'Flashlight,Iron'"})
public void test7(String str) {
     String[] itemNames = str.split(",");
     JpaSubQuery<OrderProduct, Order> subQuery =
                orderDao.query()
                        .subQuery(OrderProduct.class, "o", Order.class)
                        .join(OrderProduct::getProduct, "p", null)
                        .filter(new FilterList()
                                .eq(Product::getId, OrderProduct::getProduct)
                                .in(Product::getName, List.of(itemNames)))
                        .select(OrderProduct::getOrder);
     int rows = orderDao.delete().filter(Restrictions.exists(subQuery)).execute();
     log.info("Affected rows: {}", rows);
}
```

## Get Started

* JDK 17 or later
* Spring Boot 3.x or latest preferred
* H2, Postgresql, MySQL perfect supported
* pom.xml

``` xml
<dependency>
    <groupId>com.github.paganini2008</groupId>
    <artifactId>easyjpa-spring-boot-starter</artifactId>
    <version>1.0.0-RC1</version>  <!-- use the latest version here -->
</dependency>
```

* Java Configuration

``` java
@EntityScan(basePackages = {"com.github.easyjpa.test.entity"})
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityDaoFactoryBean.class,
        basePackages = {"com.github.easyjpa.test.dao"})
@Configuration(proxyBeanMethods = false)
public class JpaConfig {
    
}
```

* Entity Definition

```java
@Entity
@Table(name = "example_user")
public class User {
   ...
}

@Entity
@Table(name = "example_product")
public class Product {
    ...
}

@Entity
@Table(name = "example_order")
public class Order {
    ...
}
```

* DAO (Repository) Definition


##### UserDao
``` java
public interface UserDao extends EntityDao<User, Long> {

}
```
##### OrderDao
``` java
public interface OrderDao extends EntityDao<Order, Long> {

}
```
##### ProductDao
``` java
public interface ProductDao extends EntityDao<Product, Long> {

}
```



## Contribution and License

This project is open source and licensed under the **MIT License**.

## Project Link

For more information, visit the **EasyJPA GitHub repository**: [paganini2008/easyjpa](https://github.com/paganini2008/easyjpa).