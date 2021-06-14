
package com.github.easyjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Subquery;

/**
 * 
 * @Description: JpaSubQueryGroupByImpl
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
public class JpaSubQueryGroupByImpl<X, Y> implements JpaSubQueryGroupBy<X, Y> {

    private final Model<X> model;
    private final Subquery<Y> query;
    private final CriteriaBuilder builder;

    JpaSubQueryGroupByImpl(Model<X> model, Subquery<Y> query, CriteriaBuilder builder) {
        this.model = model;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public JpaSubQueryGroupBy<X, Y> having(Filter filter) {
        query.having(filter.toPredicate(model, builder));
        return this;
    }

    @Override
    public JpaSubQueryGroupBy<X, Y> select(String alias, String attributeName) {
        return select(Property.forName(alias, attributeName));
    }

    @Override
    public JpaSubQueryGroupBy<X, Y> select(Field<Y> field) {
        Expression<Y> expression = field.toExpression(model, builder);
        query.select(expression);
        return this;
    }

}
