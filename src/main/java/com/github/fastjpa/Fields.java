package com.github.fastjpa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

/**
 * 
 * @Description: Fields
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
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

    public static <T> Field<T> toLiteral(T value) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                return builder.literal(value);
            }

            public String toString() {
                return value.toString();
            }
        };
    }

    public static <E, T extends Number> Field<T> plus(SerializedFunction<E, T> leftField,
            SerializedFunction<E, T> rightField) {
        return plus(Property.forName(leftField), Property.forName(rightField));
    }

    public static <T extends Number> Field<T> plus(Field<T> leftField, Field<T> rightField) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> leftExpression = leftField.toExpression(model, builder);
                Expression<T> rightExpression = rightField.toExpression(model, builder);
                return builder.sum(leftExpression, rightExpression);
            }

            public String toString() {
                return leftField.toString() + "+" + rightField.toString();
            }
        };
    }

    public static <E, T extends Number> Field<T> plus(SerializedFunction<E, T> leftField, T value) {
        return plus(Property.forName(leftField), value);
    }

    public static <T extends Number> Field<T> plus(Field<T> field, T value) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.sum(expression, value);
            }

            public String toString() {
                return field.toString() + "+" + value;
            }
        };
    }

    public static <E, T extends Number> Field<T> minus(SerializedFunction<E, T> leftField,
            SerializedFunction<E, T> rightField) {
        return minus(Property.forName(leftField), Property.forName(rightField));
    }

    public static <T extends Number> Field<T> minus(Field<T> leftField, Field<T> rightField) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> leftExpression = leftField.toExpression(model, builder);
                Expression<T> rightExpression = rightField.toExpression(model, builder);
                return builder.diff(leftExpression, rightExpression);
            }

            public String toString() {
                return leftField.toString() + "-" + rightField.toString();
            }
        };
    }

    public static <E, T extends Number> Field<T> minus(SerializedFunction<E, T> field, T value) {
        return minus(Property.forName(field), value);
    }

    public static <T extends Number> Field<T> minus(Field<T> field, T value) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.diff(expression, value);
            }

            public String toString() {
                return field.toString() + "-" + value;
            }
        };
    }

    public static <E> Field<Number> multiply(SerializedFunction<E, Number> leftField,
            SerializedFunction<E, Number> rightField) {
        return multiply(Property.forName(leftField), Property.forName(rightField));
    }

    public static <T extends Number> Field<T> multiply(Field<T> leftField, Field<T> rightField) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> leftExpression = leftField.toExpression(model, builder);
                Expression<T> rightExpression = rightField.toExpression(model, builder);
                return builder.prod(leftExpression, rightExpression);
            }

            public String toString() {
                return leftField.toString() + "*" + rightField.toString();
            }
        };
    }

    public static <E, T extends Number> Field<T> multiply(SerializedFunction<E, T> field, T value) {
        return multiply(Property.forName(field), value);
    }

    public static <T extends Number> Field<T> multiply(Field<T> field, T value) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.prod(expression, value);
            }

            public String toString() {
                return field.toString() + "*" + value;
            }
        };
    }

    public static <E, T extends Number> Field<Number> divide(SerializedFunction<E, T> leftField,
            SerializedFunction<E, T> rightField) {
        return divide(Property.forName(leftField), Property.forName(rightField));
    }

    public static <T extends Number> Field<Number> divide(Field<T> leftField, Field<T> rightField) {
        return new Field<Number>() {

            public Expression<Number> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> leftExpression = leftField.toExpression(model, builder);
                Expression<T> rightExpression = rightField.toExpression(model, builder);
                return builder.quot(leftExpression, rightExpression);
            }

            public String toString() {
                return leftField.toString() + "/" + rightField.toString();
            }
        };
    }

    public static <E, T extends Number> Field<Number> divide(SerializedFunction<E, T> field,
            T value) {
        return divide(Property.forName(field), value);
    }

    public static <T extends Number> Field<Number> divide(Field<T> field, T value) {
        return new Field<Number>() {

            public Expression<Number> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.quot(expression, value);
            }

            public String toString() {
                return field.toString() + "/" + value;
            }
        };
    }

    public static <E, T extends Number> Field<Number> divide(T value,
            SerializedFunction<E, T> field) {
        return divide(value, Property.forName(field));
    }

    public static <T extends Number> Field<Number> divide(T value, Field<T> field) {
        return new Field<Number>() {

            public Expression<Number> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.quot(value, expression);
            }

            public String toString() {
                return value + "/" + field.toString();
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

    public static <X, T extends Number> Field<T> neg(SerializedFunction<X, T> function) {
        return neg(Property.forName(function));
    }

    public static <T extends Number> Field<T> neg(Field<T> field) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.neg(expression);
            }

            public String toString() {
                return "-1 * " + field.toString();
            }
        };
    }

    public static <X, T extends Number> Field<Double> sqrt(SerializedFunction<X, T> function) {
        return sqrt(Property.forName(function));
    }

    public static <T extends Number> Field<Double> sqrt(Field<T> field) {
        return new Field<Double>() {

            public Expression<Double> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.sqrt(expression);
            }

            public String toString() {
                return "sqrt(" + field.toString() + ")";
            }
        };
    }

    public static <X, T extends Number> Field<T> abs(SerializedFunction<X, T> function) {
        return abs(Property.forName(function));
    }

    public static <T extends Number> Field<T> abs(Field<T> field) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.abs(expression);
            }

            public String toString() {
                return "abs(" + field.toString() + ")";
            }
        };
    }

    public static <X, T extends Comparable<T>> Field<T> max(SerializedFunction<X, T> function) {
        return max(Property.forName(function));
    }

    public static <T extends Comparable<T>> Field<T> max(String attributeName,
            Class<T> requiredType) {
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

    public static <X, T extends Comparable<T>> Field<T> min(SerializedFunction<X, T> function) {
        return min(Property.forName(function));
    }

    public static <T extends Comparable<T>> Field<T> min(String attributeName,
            Class<T> requiredType) {
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

    public static <X, T extends Number> Field<T> sum(SerializedFunction<X, T> function) {
        return sum(Property.forName(function));
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

    public static <X, T extends Number> Field<Double> avg(SerializedFunction<X, T> function) {
        return avg(Property.forName(function));
    }

    public static <T extends Number> Field<Double> avg(String attributeName,
            Class<T> requiredType) {
        return avg(Property.forName(attributeName, requiredType));
    }

    public static <T extends Number> Field<Double> avg(Field<T> field) {
        return new Field<Double>() {

            public Expression<Double> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.avg(expression);
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

    public static <X> Field<Long> count(SerializedFunction<X, ?> function) {
        return count(Property.forName(function));
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

    public static <X> Field<Long> countDistinct(SerializedFunction<X, ?> function) {
        return countDistinct(Property.forName(function));
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

    public static <X, T> Field<T> coalesce(SerializedFunction<X, T> function, T value) {
        return coalesce(Property.forName(function), value);
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

    public static <X, T> Field<T> nullif(SerializedFunction<X, T> function, T value) {
        return nullif(Property.forName(function), value);
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

    public static <X> Field<String> concat(String value, SerializedFunction<X, String> function) {
        return concat(value, Property.forName(function));
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

    public static <X> Field<String> concat(SerializedFunction<X, String> function, String value) {
        return concat(Property.forName(function), value);
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

    public static <X> Field<Integer> length(SerializedFunction<X, String> function) {
        return length(Property.forName(function));
    }

    public static Field<Integer> length(Field<String> field) {
        return new Field<Integer>() {

            public Expression<Integer> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<String> expression = field.toExpression(model, builder);
                return builder.length(expression);
            }

            public String toString() {
                return "length(" + field.toString() + ")";
            }
        };

    }

    public static <X, T extends Number> Field<T> ceil(SerializedFunction<X, T> function) {
        return ceil(Property.forName(function));
    }

    public static <T extends Number> Field<T> ceil(Field<T> field) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.ceiling(expression);
            }

            public String toString() {
                return "ceil(" + field.toString() + ")";
            }
        };

    }

    public static <X, T extends Number> Field<T> floor(SerializedFunction<X, T> function) {
        return floor(Property.forName(function));
    }

    public static <T extends Number> Field<T> floor(Field<T> field) {
        return new Field<T>() {

            public Expression<T> toExpression(Model<?> model, CriteriaBuilder builder) {
                Expression<T> expression = field.toExpression(model, builder);
                return builder.floor(expression);
            }

            public String toString() {
                return "floor(" + field.toString() + ")";
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

    public static <X> Field<String> substring(SerializedFunction<X, String> function, int from,
            int to) {
        return substring(Property.forName(function), from, to);
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

    public static <X> Field<String> substring(SerializedFunction<X, String> function, int from) {
        return substring(Property.forName(function), from);
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

    public static <X> Field<String> lower(SerializedFunction<X, String> function) {
        return lower(Property.forName(function));
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

    public static <X> Field<String> upper(SerializedFunction<X, String> function) {
        return upper(Property.forName(function));
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
