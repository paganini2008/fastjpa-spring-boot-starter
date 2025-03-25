
package com.github.easyjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

/**
 * 
 * @Description: JpaUpdateImpl
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
public class JpaUpdateImpl<E> implements JpaUpdate<E> {

    private final Model<E> model;
    private final CriteriaUpdate<E> update;
    private final CriteriaBuilder builder;
    private final JpaCustomUpdate<E> customUpdate;

    JpaUpdateImpl(Model<E> model, CriteriaUpdate<E> update, CriteriaBuilder builder,
            JpaCustomUpdate<E> customUpdate) {
        this.model = model;
        this.update = update;
        this.builder = builder;
        this.customUpdate = customUpdate;
    }

    @Override
    public JpaUpdate<E> filter(Filter filter) {
        if (filter != null) {
            update.where(filter.toPredicate(model, builder));
        }
        return this;
    }

    @Override
    public <T> JpaUpdate<E> set(String attributeName, T value) {
        update.set(model.getAttribute(attributeName), value);
        return this;
    }

    @Override
    public <T> JpaUpdate<E> set(String attributeName1, T value1, String attributeName2, T value2) {
        update.set(model.getAttribute(attributeName1), value1).set(attributeName2, value2);
        return this;
    }

    @Override
    public <T> JpaUpdate<E> set(String attributeName1, T value1, String attributeName2, T value2,
            String attributeName3, T value3) {
        update.set(model.getAttribute(attributeName1), value1).set(attributeName2, value2)
                .set(attributeName3, value3);
        return this;
    }

    @Override
    public JpaUpdate<E> set(String attributeName, String anotherAttributeName) {
        return set(attributeName, Property.forName(null, anotherAttributeName));
    }

    @Override
    public <T> JpaUpdate<E> setField(String attributeName, Field<T> value) {
        Path<T> path = model.getAttribute(attributeName);
        update.set(path, value.toExpression(model, builder));
        return this;
    }

    @Override
    public <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass) {
        Subquery<X> subquery = update.subquery(entityClass);
        Root<X> root = subquery.from(entityClass);
        return new JpaSubQueryImpl<X, X>(Model.forRoot(root), subquery, builder);
    }

    @Override
    public <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass) {
        Subquery<Y> subquery = update.subquery(resultClass);
        Root<X> root = subquery.from(entityClass);
        return new JpaSubQueryImpl<X, Y>(Model.forRoot(root), subquery, builder);
    }

    @Override
    public int execute() {
        return customUpdate.executeUpdate((CriteriaBuilder builder) -> update);
    }
}
