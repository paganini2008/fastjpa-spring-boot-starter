
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

/**
 * 
 * @Description: JpaDeleteImpl
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaDeleteImpl<E> implements JpaDelete<E> {

    private final Model<E> model;
    private final CriteriaDelete<E> delete;
    private final CriteriaBuilder builder;
    private final JpaCustomUpdate<E> customUpdate;

    JpaDeleteImpl(Model<E> model, CriteriaDelete<E> delete, CriteriaBuilder builder,
            JpaCustomUpdate<E> customUpdate) {
        this.model = model;
        this.delete = delete;
        this.builder = builder;
        this.customUpdate = customUpdate;
    }

    @Override
    public JpaDelete<E> filter(Filter filter) {
        if (filter != null) {
            delete.where(filter.toPredicate(model, builder));
        }
        return this;
    }

    @Override
    public <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass) {
        Subquery<X> subquery = delete.subquery(entityClass);
        Root<X> root = subquery.from(entityClass);
        return new JpaSubQueryImpl<X, X>(Model.forRoot(root), subquery, builder);
    }

    @Override
    public <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass) {
        Subquery<Y> subquery = delete.subquery(resultClass);
        Root<X> root = subquery.from(entityClass);
        return new JpaSubQueryImpl<X, Y>(Model.forRoot(root), subquery, builder);
    }

    @Override
    public int execute() {
        return customUpdate.executeUpdate((CriteriaBuilder builder) -> delete);
    }

}
