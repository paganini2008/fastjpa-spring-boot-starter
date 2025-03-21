
package com.github.fastjpa;

import java.util.List;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaQueryResultSetImpl
 * @Author: Fred Feng
 * @Date: 20/03/2025
 * @Version 1.0.0
 */
public class JpaQueryResultSetImpl<T> implements JpaQueryResultSet<T> {

    private final Model<?> model;
    private final CriteriaQuery<T> query;
    private final JpaCustomQuery<?> customQuery;

    JpaQueryResultSetImpl(Model<?> model, CriteriaQuery<T> query, JpaCustomQuery<?> customQuery) {
        this.model = model;
        this.query = query;
        this.customQuery = customQuery;
    }

    @Override
    public List<T> list(int maxResults, long firstResult) {
        return customQuery.getResultList(builder -> query, maxResults, firstResult);
    }

    @Override
    public <R> ListableQuery<R> setTransformer(Transformer<T, R> transformer) {
        return new JpaListableQueryImpl<T, R>(model, query, customQuery, transformer);
    }
}
