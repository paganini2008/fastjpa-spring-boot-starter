
package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;
import com.github.fastjpa.page.PageableQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Selection;

/**
 * 
 * @Description: JpaGroupPageableQueryImpl
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaGroupPageableQueryImpl<T, R> implements PageableQuery<R> {

    private final Model<?> model;
    private final CriteriaQuery<T> query;
    private final CriteriaQuery<Long> counter;
    private final JpaCustomQuery<?> customQuery;
    private final Transformer<T, R> transformer;

    JpaGroupPageableQueryImpl(Model<?> model, CriteriaQuery<T> query, CriteriaQuery<Long> counter,
            JpaCustomQuery<?> customQuery, Transformer<T, R> transformer) {
        this.model = model;
        this.query = query;
        this.counter = counter;
        this.customQuery = customQuery;
        this.transformer = transformer;
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
    public List<R> list(int maxResults, long firstResult) {
        List<T> list = customQuery.getResultList(builder -> query, maxResults, firstResult);
        List<R> results = new ArrayList<R>();
        List<Selection<?>> selections = query.getSelection().getCompoundSelectionItems();
        for (T t : list) {
            R data = transformer.transfer(model, selections, t);
            results.add(data);
        }
        return results;
    }
}
