# Fastjpa-spring-boot-starter
A very cool JPA developping tookit, which provides an API  to use  JPA core class with a  fluent style. It aims to promote to use JPA in  object-oriented way rather than to  write JQL or HQL in your code. In fact, <code>fastjpa-spring-boot-starter</code> is  rather easier than <code>spring-boot-starter-data-jpa</code> in API's complexity

## Install

``` xml
<dependency>
  <groupId>com.github.paganini2008.springworld</groupId>
  <artifactId>fastjpa-spring-boot-starter</artifactId>
  <version>2.0.1</version>
</dependency>

```

## Compatibility

* Jdk1.8 (or later)
* <code>SpringBoot</code> Framework 2.2.x  (or later)
* Hibernate 5.x (or later)



## Core API

- EntityDao
- Model
- JpaQuery
- JpaPage
- Filter
- Column
- Field
- JpaGroupBy
- JpaSort
- JpaPageResultSet
- JpaQueryResultSet
- JpaUpdate
- JpaDelete




## Quick Start

#### 1. Materials

<code>User.java</code>

``` java
@Getter
@Setter
@Entity
@Table(name = "demo_user")
public class User {
	
	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 45)
	private String name;
	
	@Column(name = "phone", nullable = false, length = 45)
	private String phone;
	
	@Column(name = "vip", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	private Boolean vip;

}
```

<code>Product.java</code>

``` java
@Getter
@Setter
@Entity
@Table(name = "demo_product")
public class Product {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	@Column(name = "price", nullable = false, precision = 11, scale = 2)
	private BigDecimal price;

	@Column(name = "origin", nullable = true, length = 225)
	private String origin;

}
```

<code>Order.java</code>

``` java
@Getter
@Setter
@Entity
@Table(name = "demo_order")
public class Order {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "discount", nullable = true)
	private Float discount;

	@Column(name = "price", nullable = false, precision = 11, scale = 2)
	private BigDecimal price;

	@OneToOne(targetEntity = Product.class)
	@JoinColumn(nullable = false, name = "product_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	private Product product;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", columnDefinition = "timestamp null ")
	private Date createTime;

}
```

#### 2. Define Repository Class

<code>UserDao.java</code>

``` java
public interface UserDao extends EntityDao<User, Long> {
}
```

<code>OrderDao.java</code>

``` java
public interface OrderDao extends EntityDao<Order, Long> {
}
```

<code>ProductDao.java</code>

``` java
public interface ProductDao extends EntityDao<Product, Long> {
}
```

#### 3. Where Clause

*Comparison operators*

``` java
LogicalFilter filter = Restrictions.between("price", 10, 50);
filter = Restrictions.in("price", Arrays.asList(10,20,30,40,50));
filter =Restrictions.like("name", "%jpa%");
filter = Restrictions.eq("orignal", "Sydney");
```
*Logical operators*

``` java
LogicalFilter filter = Restrictions.between("price", 10, 50);
filter = filter.and(Restrictions.like("name", "%jpa%"));
filter = filter.and(Restrictions.eq("orignal", "Sydney"));
```
*Example*

``` java
LogicalFilter filter = Restrictions.gt("price", 50);
productDao.query().filter(filter).selectThis().list().forEach(prod -> {
	System.out.println(prod);
});
// Hibernate: select product0_.id as id1_1_, product0_.name as name2_1_, product0_.origin as origin3_1_, product0_.price as price4_1_ from demo_product product0_ where product0_.price>50.0
```
#### 4. Group Clause

``` java
productDao.multiquery().groupBy("origin").select(Column.forName("origin"), Fields.count(Fields.toInteger(1)).as("count")).list().forEach(t -> {
	System.out.println("origin: "+t.get("origin") + "\tcount: " + t.get("count"));
});
// Hibernate: select product0_.origin as col_0_0_, count(1) as col_1_0_ from demo_product product0_ group by product0_.origin

```
#### 5. Order Clause

``` java
orderDao.query().filter(Restrictions.gte("price", 50)).sort(JpaSort.desc("createTime"), JpaSort.asc("price")).selectThis().list(10).forEach(pro -> {
					System.out.println(pro);
				});
// Hibernate: select order0_.id as id1_0_, order0_.create_time as create_t2_0_, order0_.discount as discount3_0_, order0_.price as price4_0_, order0_.product_id as product_6_0_, order0_.receiver as receiver5_0_, order0_.user_id as user_id7_0_ from demo_order order0_ where order0_.price>=50.0 order by order0_.create_time desc, order0_.price asc limit ?
```

#### 6. Join Clause

*Left Join*

``` java
PageResponse<Tuple> pageResponse = orderDao.multiselect().leftJoin("product", "p")
	.filter(Restrictions.gte("p", "price", 50)).sort(JpaSort.desc("createTime")).selectAlias("p")
				.list(PageRequest.of(10));
		for (PageResponse<Tuple> current : pageResponse) {
			System.out.println("Current Page: " + current.getPageNumber());
			for (Tuple tuple : current.getContent()) {
				System.out.println(Arrays.toString(tuple.toArray()));
			}
		}
// Hibernate: select product1_.id as id1_1_, product1_.name as name2_1_, product1_.origin as origin3_1_, product1_.price as price4_1_ from demo_order order0_ left outer join demo_product product1_ on order0_.product_id=product1_.id where product1_.price>=50.0 order by order0_.create_time desc limit ?, ?
```

*Inner Join*

``` java
		ColumnList columnList = new ColumnList();
		columnList.addColumn("id");
		columnList.addColumn("p","name");
		columnList.addColumn(Property.forName("p", "price"),"originalPrice");
		columnList.addColumn("price");
		columnList.addColumn("createTime");
		PageResponse<Tuple> pageResponse = orderDao.multiselect().join("product", "p")
				.filter(Restrictions.gte("p", "price", 50)).sort(JpaSort.desc("createTime")).select(columnList)
				.list(PageRequest.of(10));
		for (PageResponse<Tuple> current : pageResponse) {
			System.out.println("Current Page: " + current.getPageNumber());
			for (Tuple tuple : current.getContent()) {
				System.out.println(Arrays.toString(tuple.toArray()));
			}
		}
// Hibernate: select order0_.id as col_0_0_, product1_.name as col_1_0_, product1_.price as col_2_0_, order0_.price as col_3_0_, order0_.create_time as col_4_0_ from demo_order order0_ inner join demo_product product1_ on order0_.product_id=product1_.id where product1_.price>=50.0 order by order0_.create_time desc limit ?, ?
```

#### 7. Function Clause

*Aggregation Function*

``` java
ColumnList columnList = new ColumnList()
				.addColumn(Fields.max("price", BigDecimal.class), "maxPrice")
				.addColumn(Fields.min("price", BigDecimal.class), "minPrice")
				.addColumn(Fields.avg("price", Double.class), "avgPrice")
				.addColumn(Fields.count(Fields.toInteger(1)), "count")
				.addColumn("origin");
productDao.multiquery().groupBy("origin").select(columnList).setTransformer(Transformers.asBean(ProductVO.class)).list().forEach(vo -> {
	System.out.println(vo);
});
```
*Other Common Function*

``` java
ColumnList columnList = new ColumnList()
       .addColumn(Function.build("LOWER", String.class, "name"), "name")
       .addColumn(Function.build("UPPER", String.class, "origin"), "origin");
productDao.multiquery().select(columnList).list(10).forEach(t -> {
	System.out.println("name: " + t.get("name") + "\t origin: " + t.get("origin"));
});
// Hibernate: select lower(product0_.name) as col_0_0_, upper(product0_.origin) as col_1_0_ from demo_product product0_ limit ?
```

*Case When*

``` java
IfExpression<String, String> ifExpression = new IfExpression<String, String>(Property.forName("origin", String.class));
		ifExpression = ifExpression.when("Shanghai", "Asia")
				                   .when("Tokyo", "Asia")
				                   .when("New York", "North America")
				                   .when("Washington", "North America")
				                   .otherwise("Other Area");
		ColumnList columnList = new ColumnList().addColumn(ifExpression, "Area")
				                                .addColumn(Fields.count(Fields.toInteger(1)), "Count");
		productDao.multiquery().groupBy(Fields.toInteger(1)).select(columnList).list().forEach(t -> {
			System.out.println("Area: " + t.get(0) + "\t Count: " + t.get(1));
		});
// Hibernate: select case product0_.origin when 'Shanghai' then 'Asia' when 'Tokyo' then 'Asia' when 'New York' then 'North America' when 'Washington' then 'North America' else 'Other Area' end as col_0_0_, count(1) as col_1_0_ from demo_product product0_ group by 1
```

####  8. SubQuery Clause

``` java
JpaQuery<Order,Order> jpaQuery = orderDao.query();
JpaSubQuery<Product, BigDecimal> subQuery = jpaQuery.subQuery(Product.class, "p", BigDecimal.class)
				.select(Fields.avg(Property.forName("p", "price")));
jpaQuery.filter(Restrictions.gte("price", subQuery)).selectThis().list(10).forEach(prod -> {
			System.out.println(prod);
});
// Hibernate: select order0_.id as id1_0_, order0_.create_time as create_t2_0_, order0_.discount as discount3_0_, order0_.price as price4_0_, order0_.product_id as product_6_0_, order0_.receiver as receiver5_0_, order0_.user_id as user_id7_0_ from demo_order order0_ where order0_.price>=(select avg(product1_.price) from demo_product product1_) limit ?
```

#### 



