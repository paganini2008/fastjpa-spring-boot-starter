/**
* Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)

* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.github.paganini2008.springworld.fastjpa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 * 
 * Fields
 *
 * @author Fred Feng
 * @version 1.0
 */
public abstract class Fields {

	public static <X> Field<X> root() {
		return new Field<X>() {

			@SuppressWarnings("unchecked")
			public Expression<X> toExpression(Model<?> model, CriteriaBuilder builder) {
				return (Expression<X>) model.getRoot();
			}

			public String toString() {
				return Model.ROOT;
			}
		};
	}

	public static Field<BigDecimal> toBigDecimal(BigDecimal value) {
		return new Field<BigDecimal>() {
			public Expression<BigDecimal> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toBigDecimal(builder.literal(value));
			}

			public String toString() {
				return value.toPlainString();
			}
		};
	}

	public static Field<BigInteger> toBigInteger(BigInteger value) {
		return new Field<BigInteger>() {
			public Expression<BigInteger> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toBigInteger(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static Field<Integer> toInteger(Integer value) {
		return new Field<Integer>() {
			public Expression<Integer> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toInteger(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static Field<Long> toLong(Long value) {
		return new Field<Long>() {
			public Expression<Long> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toLong(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static Field<Float> toFloat(Float value) {

		return new Field<Float>() {
			public Expression<Float> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toFloat(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static Field<Double> toDouble(Double value) {
		return new Field<Double>() {
			public Expression<Double> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toDouble(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static Field<String> toString(Character value) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.toString(builder.literal(value));
			}

			public String toString() {
				return value.toString();
			}
		};
	}

	public static <T extends Comparable<T>> Field<T> max(String attributeName, Class<T> requiredType) {
		return max(Property.forName(attributeName, requiredType));
	}

	public static <T extends Comparable<T>> Field<T> max(Field<T> field) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.greatest(expression);
			}

			public String toString() {
				return "max(" + field.toString() + ")";
			}
		};
	}

	public static <T extends Comparable<T>> Field<T> min(String attributeName, Class<T> requiredType) {
		return min(Property.forName(attributeName, requiredType));
	}

	public static <T extends Comparable<T>> Field<T> min(Field<T> field) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.least(expression);
			}

			public String toString() {
				return "min(" + field.toString() + ")";
			}
		};
	}

	public static <T extends Number> Field<T> sum(String attributeName, Class<T> requiredType) {
		return sum(Property.forName(attributeName, requiredType));
	}

	public static <T extends Number> Field<T> sum(Field<T> field) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.sum(expression);
			}

			public String toString() {
				return "sum(" + field.toString() + ")";
			}
		};
	}

	public static <T extends Number> Field<T> avg(String attributeName, Class<T> requiredType) {
		return avg(Property.forName(attributeName, requiredType));
	}

	public static <T extends Number> Field<T> avg(Field<T> field) {
		return new Field<T>() {
			@SuppressWarnings("unchecked")
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return (Expression<T>) builder.avg(expression);
			}

			public String toString() {
				return "avg(" + field.toString() + ")";
			}
		};
	}

	public static Field<Long> count(int i) {
		return count(toInteger(i));
	}

	public static Field<Long> count(String attributeName) {
		return count(Property.forName(attributeName));
	}

	public static Field<Long> count(Field<?> field) {
		return new Field<Long>() {
			public Expression<Long> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<?> expression = field.toExpression(model, builder);
				return builder.count(expression);
			}

			public String toString() {
				return "count(" + field.toString() + ")";
			}
		};
	}

	public static Field<Long> count(SubQueryBuilder<?> subQueryBuilder) {
		return new Field<Long>() {
			public Expression<Long> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.count(subQueryBuilder.toSubquery(builder));
			}

			public String toString() {
				return "count(subquery)";
			}
		};
	}

	public static Field<Long> countDistinct(String attributeName) {
		return countDistinct(Property.forName(attributeName));
	}

	public static Field<Long> countDistinct(Field<?> field) {
		return new Field<Long>() {
			public Expression<Long> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<?> expression = field.toExpression(model, builder);
				return builder.countDistinct(expression);
			}

			public String toString() {
				return "count(distinct " + field.toString() + ")";
			}
		};
	}

	public static Field<Long> countDistinct(SubQueryBuilder<?> subQueryBuilder) {
		return new Field<Long>() {
			public Expression<Long> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.countDistinct(subQueryBuilder.toSubquery(builder));
			}

			public String toString() {
				return "count(distinct subquery)";
			}
		};
	}

	public static <T> Field<T> some(SubQueryBuilder<T> subquery) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.some(subquery.toSubquery(builder));
			}

			public String toString() {
				return "some (" + subquery + ")";
			}
		};
	}

	public static <T> Field<T> any(SubQueryBuilder<T> subquery) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.any(subquery.toSubquery(builder));
			}

			public String toString() {
				return "any (" + subquery + ")";
			}
		};
	}

	public static <T> Field<T> all(SubQueryBuilder<T> subquery) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.all(subquery.toSubquery(builder));
			}

			public String toString() {
				return "all (" + subquery + ")";
			}
		};
	}

	public static Field<Integer> mod(Field<Integer> field, Field<Integer> otherField) {
		return new Field<Integer>() {
			public Expression<Integer> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<Integer> expression = field.toExpression(model, builder);
				return builder.mod(expression, otherField.toExpression(model, builder));
			}

			public String toString() {
				return "mod(" + field.toString() + "," + otherField.toString() + ")";
			}
		};
	}

	public static Field<Integer> mod(Field<Integer> field, Integer value) {
		return new Field<Integer>() {
			public Expression<Integer> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<Integer> expression = field.toExpression(model, builder);
				return builder.mod(expression, value);
			}

			public String toString() {
				return "mod(" + field.toString() + "," + value + ")";
			}
		};
	}

	public static Field<Integer> mod(Integer value, Field<Integer> field) {
		return new Field<Integer>() {
			public Expression<Integer> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<Integer> expression = field.toExpression(model, builder);
				return builder.mod(value, expression);
			}

			public String toString() {
				return "mod(" + value + "," + field.toString() + ")";
			}
		};
	}

	public static <T extends Number> Field<T> neg(Field<T> field) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.neg(expression);
			}

			public String toString() {
				return "neg(" + field.toString() + ")";
			}
		};
	}

	public static <T> Field<T> coalesce(Field<T> field, Field<T> anotherField) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				Expression<T> anotherExpression = field.toExpression(model, builder);
				return builder.coalesce(expression, anotherExpression);
			}

			public String toString() {
				return "coalesce(" + field.toString() + "," + anotherField + ")";
			}
		};
	}

	public static <T> Field<T> coalesce(Field<T> field, T value) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.coalesce(expression, value);
			}

			public String toString() {
				return "coalesce(" + field.toString() + "," + value + ")";
			}
		};
	}

	public static <T> Field<T> nullif(Field<T> field, T value) {
		return new Field<T>() {
			public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<T> expression = field.toExpression(model, builder);
				return builder.nullif(expression, value);
			}

			public String toString() {
				return "nullif(" + field.toString() + "," + value + ")";
			}
		};
	}

	public static Field<java.sql.Date> currentDate() {
		return new Field<java.sql.Date>() {
			public Expression<java.sql.Date> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.currentDate();
			}

			public String toString() {
				return "current_date()";
			}
		};
	}

	public static Field<java.sql.Time> currentTime() {
		return new Field<java.sql.Time>() {
			public Expression<java.sql.Time> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.currentTime();
			}

			public String toString() {
				return "current_time()";
			}
		};
	}

	public static Field<Timestamp> currentTimestamp() {
		return new Field<Timestamp>() {
			public Expression<Timestamp> toExpression(Model<?> model, CriteriaBuilder builder) {
				return builder.currentTimestamp();
			}

			public String toString() {
				return "current_timestamp()";
			}
		};

	}

	public static Field<String> concat(Field<String> left, Field<String> right) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> leftExpression = left.toExpression(model, builder);
				Expression<String> rightExpression = right.toExpression(model, builder);
				return builder.concat(leftExpression, rightExpression);
			}

			public String toString() {
				return "concat(" + left.toString() + "," + right.toString() + ")";
			}
		};

	}

	public static Field<String> concat(String value, Field<String> field) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.concat(value, expression);
			}

			public String toString() {
				return "concat(" + value + "," + field.toString() + ")";
			}
		};

	}

	public static Field<String> concat(Field<String> field, String value) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.concat(expression, value);
			}

			public String toString() {
				return "concat(" + field.toString() + "," + value + ")";
			}
		};

	}

	public static Field<String> substring(Field<String> field, Field<Integer> anotherField) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				Expression<Integer> anotherExpression = anotherField.toExpression(model, builder);
				return builder.substring(expression, anotherExpression);
			}

			public String toString() {
				return "substring(" + field.toString() + "," + anotherField.toString() + ")";
			}
		};

	}

	public static Field<String> substring(Field<String> field, int from, int to) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.substring(expression, from, to);
			}

			public String toString() {
				return "substring(" + field.toString() + "," + from + "," + to + ")";
			}
		};

	}

	public static Field<String> substring(Field<String> field, int from) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.substring(expression, from);
			}

			public String toString() {
				return "substring(" + field.toString() + "," + from + ")";
			}
		};

	}

	public static Field<String> lower(Field<String> field) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.lower(expression);
			}

			public String toString() {
				return "lower(" + field.toString() + ")";
			}
		};

	}

	public static Field<String> upper(Field<String> field) {
		return new Field<String>() {
			public Expression<String> toExpression(Model<?> model, CriteriaBuilder builder) {
				Expression<String> expression = field.toExpression(model, builder);
				return builder.upper(expression);
			}

			public String toString() {
				return "upper(" + field.toString() + ")";
			}
		};

	}

}
