package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;

/**
 * 
 * @Description: JpaSort
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaSort {

    Order toOrder(Model<?> model, CriteriaBuilder builder);

    static JpaSort asc(String attributeName) {
        return asc(Property.forName(attributeName));
    }

    static JpaSort asc(String alias, String attributeName) {
        return asc(Property.forName(alias, attributeName));
    }

    static <X, Y> JpaSort asc(SerializedFunction<X, Y> sf) {
        return asc(Property.forName(sf));
    }

    static JpaSort asc(Field<?> field) {
        return new JpaSort() {

            public Order toOrder(Model<?> model, CriteriaBuilder builder) {
                return builder.asc(field.toExpression(model, builder));
            }
        };
    }

    static JpaSort desc(String attributeName) {
        return desc(Property.forName(attributeName));
    }

    static JpaSort desc(String alias, String attributeName) {
        return desc(Property.forName(alias, attributeName));
    }

    static <X, Y> JpaSort desc(SerializedFunction<X, Y> sf) {
        return asc(Property.forName(sf));
    }

    static JpaSort desc(Field<?> field) {
        return new JpaSort() {

            public Order toOrder(Model<?> model, CriteriaBuilder builder) {
                return builder.desc(field.toExpression(model, builder));
            }
        };
    }

}
