
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaPageGroupByImpl
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaPageGroupByImpl<E, T> implements JpaPageGroupBy<E, T> {

    private final JpaGroupBy<E, T> query;
    private final JpaGroupBy<E, Long> counter;
    private final JpaCustomQuery<?> customQuery;

    JpaPageGroupByImpl(JpaGroupBy<E, T> query, JpaGroupBy<E, Long> counter,
            JpaCustomQuery<?> customQuery) {
        this.query = query;
        this.counter = counter;
        this.customQuery = customQuery;
    }

    @Override
    public JpaPageGroupBy<E, T> having(Filter expression) {
        query.having(expression);
        counter.having(expression);
        return this;

    }

    @Override
    public JpaPageResultSet<T> select(ColumnList columnList) {
        query.select(columnList);
        return new JpaGroupPageResultSet<T>(query.model(), query.query(), counter.query(),
                customQuery);
    }

    @Override
    public JpaPageGroupBy<E, T> sort(JpaSort... sorts) {
        query.sort(sorts);
        return this;
    }

    @Override
    public CriteriaQuery<T> query() {
        return query.query();
    }

    @Override
    public Model<E> model() {
        return query.model();
    }

}
