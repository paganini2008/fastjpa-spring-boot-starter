
package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Selection;

/**
 * 
 * @Description: JpaListableQueryImpl
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public class JpaListableQueryImpl<T, R> implements ListableQuery<R> {

    private final Model<?> model;
    private final CriteriaQuery<T> query;
    private final JpaCustomQuery<?> customQuery;
    private final Transformer<T, R> transformer;

    JpaListableQueryImpl(Model<?> model, CriteriaQuery<T> query, JpaCustomQuery<?> customQuery,
            Transformer<T, R> transformer) {
        this.model = model;
        this.query = query;
        this.customQuery = customQuery;
        this.transformer = transformer;
    }

    @Override
    public List<R> list(int maxResults, long firstResult) {
        List<R> results = new ArrayList<R>();
        List<T> list = customQuery.getResultList(builder -> query, maxResults, firstResult);
        if (query.getResultType() == model.getRootType()) {
            for (T original : list) {
                R data = transformer.transfer(model, original);
                results.add(data);
            }
        } else {
            List<Selection<?>> selections = query.getSelection().getCompoundSelectionItems();
            for (T original : list) {
                R data = transformer.transfer(model, selections, original);
                results.add(data);
            }
        }
        return results;
    }
}
