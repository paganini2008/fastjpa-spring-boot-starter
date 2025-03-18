package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * @Description: AndFilter
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class AndFilter extends LogicalFilter {

    private final Filter filter;

    private final Filter otherFilter;

    public AndFilter(Filter filter, Filter otherFilter) {
        this.filter = filter;
        this.otherFilter = otherFilter;
    }

    @Override
    public Predicate toPredicate(Model<?> model, CriteriaBuilder builder) {
        Predicate left = filter.toPredicate(model, builder);
        Predicate right = otherFilter.toPredicate(model, builder);
        return builder.and(new Predicate[]{left, right});
    }

}
