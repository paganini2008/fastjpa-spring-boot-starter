
package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Subquery;

/**
 * 
 * @Description: JpaSubQueryImpl
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaSubQueryImpl<X, Y> implements JpaSubQuery<X, Y> {

    private final Model<X> model;
    private final Subquery<Y> query;
    private final CriteriaBuilder builder;

    JpaSubQueryImpl(Model<X> model, Subquery<Y> query, CriteriaBuilder builder) {
        this.model = model;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public JpaSubQuery<X, Y> filter(Filter filter) {
        query.where(filter.toPredicate(model, builder));
        return this;
    }

    @Override
    public JpaSubQueryGroupBy<X, Y> groupBy(FieldList fieldList) {
        List<Expression<?>> paths = new ArrayList<Expression<?>>();
        for (Field<?> field : fieldList) {
            paths.add(field.toExpression(model, builder));
        }
        query.groupBy(paths);
        return new JpaSubQueryGroupByImpl<X, Y>(model, query, builder);
    }

    @Override
    public JpaSubQuery<X, Y> select(String alias, String attributeName) {
        return select(Property.forName(alias, attributeName));
    }

    @Override
    public JpaSubQuery<X, Y> select(Field<Y> field) {
        Expression<Y> expression = field.toExpression(model, builder);
        query.select(expression);
        return this;
    }

    @Override
    public JpaSubQuery<X, Y> distinct(boolean distinct) {
        query.distinct(distinct);
        return this;
    }

    @Override
    public <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias, Filter on) {
        Model<Z> join = model.join(attributeName, alias,
                on != null ? on.toPredicate(model, builder) : null);
        return new JpaSubQueryImpl<Z, Y>(join, query, builder);
    }

    @Override
    public <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias, Filter on) {
        Model<Z> join = model.leftJoin(attributeName, alias,
                on != null ? on.toPredicate(model, builder) : null);
        return new JpaSubQueryImpl<Z, Y>(join, query, builder);
    }

    @Override
    public <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias, Filter on) {
        Model<Z> join = model.rightJoin(attributeName, alias,
                on != null ? on.toPredicate(model, builder) : null);
        return new JpaSubQueryImpl<Z, Y>(join, query, builder);
    }

    @Override
    public Subquery<Y> toSubquery(CriteriaBuilder builder) {
        return query;
    }

}
