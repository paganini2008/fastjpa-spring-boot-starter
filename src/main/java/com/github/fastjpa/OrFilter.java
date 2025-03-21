
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * @Description: OrFilter
 * @Author: Fred Feng
 * @Date: 20/03/2025
 * @Version 1.0.0
 */
public class OrFilter extends LogicalFilter {

    private final Filter filter;

    private final Filter otherFilter;

    public OrFilter(Filter filter, Filter otherFilter) {
        this.filter = filter;
        this.otherFilter = otherFilter;
    }

    @Override
    public Predicate toPredicate(Model<?> selector, CriteriaBuilder builder) {
        Predicate left = filter.toPredicate(selector, builder);
        Predicate right = otherFilter.toPredicate(selector, builder);
        return builder.or(left, right);
    }

}
