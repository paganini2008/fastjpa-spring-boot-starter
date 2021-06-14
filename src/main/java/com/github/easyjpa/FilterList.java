package com.github.easyjpa;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * @Description: FilterList
 * @Author: Fred Feng
 * @Date: 20/10/2024
 * @Version 1.0.0
 */
public class FilterList implements Filter {

    public FilterList() {}

    public FilterList(Filter... filters) {
        this(filters != null ? List.of(filters) : Collections.emptyList());
    }

    public FilterList(Collection<Filter> filters) {
        if (filters != null && filters.size() > 0) {
            filters.forEach(this::join);
        }
    }

    private LogicalFilter logicalFilter;
    private boolean andOr = true;

    public <X> FilterList eq(SerializedFunction<X, ?> function, Object value) {
        return join(Restrictions.eq(function, value));
    }

    public <X, T> FilterList eq(SerializedFunction<X, T> function, SubQueryBuilder<T> subQuery) {
        return join(Restrictions.eq(function, subQuery));
    }

    public <X, Y> FilterList eq(SerializedFunction<X, ?> leftFun,
            SerializedFunction<Y, ?> rightFun) {
        return join(Restrictions.eq(leftFun, rightFun));
    }

    public <X> FilterList ne(SerializedFunction<X, ?> function, Object value) {
        return join(Restrictions.ne(function, value));
    }

    public <X, T> FilterList ne(SerializedFunction<X, T> function, SubQueryBuilder<T> subQuery) {
        return join(Restrictions.ne(function, subQuery));
    }

    public <X, R extends Comparable<R>> FilterList lt(SerializedFunction<X, R> function, R value) {
        return join(Restrictions.lt(function, value));
    }

    public <X, R extends Comparable<R>> FilterList lt(SerializedFunction<X, R> function,
            SubQueryBuilder<R> subQuery) {
        return join(Restrictions.lt(function, subQuery));
    }

    public <X, R extends Comparable<R>> FilterList lte(SerializedFunction<X, R> function, R value) {
        return join(Restrictions.lte(function, value));
    }

    public <X, R extends Comparable<R>> FilterList lte(SerializedFunction<X, R> function,
            SubQueryBuilder<R> subQuery) {
        return join(Restrictions.lt(function, subQuery));
    }

    public <X, R extends Comparable<R>> FilterList gt(SerializedFunction<X, R> function, R value) {
        return join(Restrictions.gt(function, value));
    }

    public <X, R extends Comparable<R>> FilterList gte(SerializedFunction<X, R> function, R value) {
        return join(Restrictions.gte(function, value));
    }

    public <X, T> FilterList in(SerializedFunction<X, T> function, Iterable<T> values) {
        return join(Restrictions.in(function, values));
    }

    public <X, T extends Comparable<T>> FilterList between(SerializedFunction<X, T> function,
            T startValue, T endValue) {
        return join(Restrictions.between(function, startValue, endValue));
    }

    public <X> FilterList like(SerializedFunction<X, String> function, String pattern) {
        return join(Restrictions.like(function, pattern));
    }

    public <X> FilterList like(SerializedFunction<X, String> function, String pattern,
            char escapeChar) {
        return join(Restrictions.like(function, pattern, escapeChar));
    }

    public <X> FilterList notLike(SerializedFunction<X, String> function, String pattern) {
        return join(Restrictions.notLike(function, pattern));
    }

    public <X> FilterList notLike(SerializedFunction<X, String> function, String pattern,
            char escapeChar) {
        return join(Restrictions.notLike(function, pattern, escapeChar));
    }

    public <X> FilterList notNull(SerializedFunction<X, ?> function) {
        return join(Restrictions.notNull(function));
    }

    public <X> FilterList isNull(SerializedFunction<X, ?> function) {
        return join(Restrictions.isNull(function));
    }

    public FilterList not() {
        logicalFilter = logicalFilter.not();
        return this;
    }

    public FilterList and() {
        andOr = true;
        return this;
    }

    public FilterList or() {
        andOr = false;
        return this;
    }

    public FilterList and(Supplier<FilterList> supplier) {
        andOr = true;
        return join(supplier.get());
    }

    public FilterList or(Supplier<FilterList> supplier) {
        andOr = false;
        return join(supplier.get());
    }

    public FilterList join(Filter filter) {
        if (logicalFilter != null) {
            logicalFilter = andOr ? logicalFilter.and(filter) : logicalFilter.or(filter);
        } else {
            logicalFilter = filter instanceof LogicalFilter ? (LogicalFilter) filter
                    : andOr ? Restrictions.juction().and(filter)
                            : Restrictions.disjuction().or(filter);
        }
        return this;
    }


    @Override
    public Predicate toPredicate(Model<?> model, CriteriaBuilder builder) {
        return logicalFilter.toPredicate(model, builder);
    }

}
