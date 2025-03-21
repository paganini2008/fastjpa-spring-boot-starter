
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Selection;

/**
 * 
 * @Description: Field
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface Field<T> {

    Expression<T> toExpression(Model<?> model, CriteriaBuilder builder);

    default Column as(final String alias) {
        return new Column() {

            public Selection<?> toSelection(Model<?> model, CriteriaBuilder builder) {
                return Field.this.toExpression(model, builder).alias(alias);
            }

            public String toString() {
                return Field.this.toString();
            }
        };
    }
}
