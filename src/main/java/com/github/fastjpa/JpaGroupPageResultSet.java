
package com.github.fastjpa;

import java.util.List;
import com.github.fastjpa.page.PageableQuery;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaGroupPageResultSet
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaGroupPageResultSet<T> implements JpaPageResultSet<T> {

    private final Model<?> model;
    private final CriteriaQuery<T> query;
    private final CriteriaQuery<Long> counter;
    private final JpaCustomQuery<?> customQuery;

    JpaGroupPageResultSet(Model<?> model, CriteriaQuery<T> query, CriteriaQuery<Long> counter,
            JpaCustomQuery<?> customQuery) {
        this.model = model;
        this.query = query;
        this.counter = counter;
        this.customQuery = customQuery;
    }

    @Override
    public List<T> list(int maxResults, long firstResult) {
        return customQuery.getResultList(builder -> query, maxResults, firstResult);
    }

    @Override
    public long rowCount() {
        List<Long> list = customQuery.getResultList(builder -> {
            counter.select(builder.count(builder.toInteger(builder.literal(1))));
            return counter;
        });
        return list != null ? list.size() : 0;
    }

    @Override
    public <R> PageableQuery<R> setTransformer(Transformer<T, R> transformer) {
        return new JpaGroupPageableQueryImpl<T, R>(model, query, counter, customQuery, transformer);
    }

}
