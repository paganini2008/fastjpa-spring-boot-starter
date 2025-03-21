package com.github.fastjpa;

import java.util.function.Supplier;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * 
 * @Description: LambdaFilter
 * @Author: Fred Feng
 * @Date: 20/03/2025
 * @Version 1.0.0
 */
public class LambdaFilter implements Filter {

    public LambdaFilter() {}

    private LogicalFilter logicalFilter;
    private boolean andOr = true;

    public <X> LambdaFilter eq(SerializedFunction<X, ?> sf, Object value) {
        return join(Restrictions.eq(sf, value));
    }

    public <X, T> LambdaFilter eq(SerializedFunction<X, T> sf, SubQueryBuilder<T> subQuery) {
        return join(Restrictions.eq(sf, subQuery));
    }

    public <X> LambdaFilter ne(SerializedFunction<X, ?> sf, Object value) {
        return join(Restrictions.ne(sf, value));
    }

    public <X, T> LambdaFilter ne(SerializedFunction<X, T> sf, SubQueryBuilder<T> subQuery) {
        return join(Restrictions.ne(sf, subQuery));
    }

    public <X, R extends Comparable<R>> LambdaFilter lt(SerializedFunction<X, R> sf, R value) {
        return join(Restrictions.lt(sf, value));
    }

    public <X, R extends Comparable<R>> LambdaFilter lt(SerializedFunction<X, R> sf,
            SubQueryBuilder<R> subQuery) {
        return join(Restrictions.lt(sf, subQuery));
    }

    public <X, R extends Comparable<R>> LambdaFilter lte(SerializedFunction<X, R> sf, R value) {
        return join(Restrictions.lte(sf, value));
    }

    public <X, R extends Comparable<R>> LambdaFilter lte(SerializedFunction<X, R> sf,
            SubQueryBuilder<R> subQuery) {
        return join(Restrictions.lt(sf, subQuery));
    }

    public <X, R extends Comparable<R>> LambdaFilter gt(SerializedFunction<X, R> sf, R value) {
        return join(Restrictions.gt(sf, value));
    }

    public <X, R extends Comparable<R>> LambdaFilter gte(SerializedFunction<X, R> sf, R value) {
        return join(Restrictions.gte(sf, value));
    }

    public <X, T> LambdaFilter in(SerializedFunction<X, T> sf, Iterable<T> values) {
        return join(Restrictions.in(sf, values));
    }

    public <X, T extends Comparable<T>> LambdaFilter between(SerializedFunction<X, T> sf,
            T startValue, T endValue) {
        return join(Restrictions.between(sf, startValue, endValue));
    }

    public <X> LambdaFilter like(SerializedFunction<X, String> sf, String pattern) {
        return join(Restrictions.like(sf, pattern));
    }

    public <X> LambdaFilter like(SerializedFunction<X, String> sf, String pattern,
            char escapeChar) {
        return join(Restrictions.like(sf, pattern, escapeChar));
    }

    public <X> LambdaFilter notLike(SerializedFunction<X, String> sf, String pattern) {
        return join(Restrictions.notLike(sf, pattern));
    }

    public <X> LambdaFilter notLike(SerializedFunction<X, String> sf, String pattern,
            char escapeChar) {
        return join(Restrictions.notLike(sf, pattern, escapeChar));
    }

    public <X> LambdaFilter notNull(SerializedFunction<X, ?> sf) {
        return join(Restrictions.notNull(sf));
    }

    public <X> LambdaFilter isNull(SerializedFunction<X, ?> sf) {
        return join(Restrictions.isNull(sf));
    }

    public LambdaFilter not() {
        logicalFilter = logicalFilter.not();
        return this;
    }

    public LambdaFilter and() {
        andOr = true;
        return this;
    }

    public LambdaFilter or() {
        andOr = false;
        return this;
    }

    public LambdaFilter and(Supplier<LambdaFilter> supplier) {
        andOr = true;
        return join(supplier.get());
    }

    public LambdaFilter or(Supplier<LambdaFilter> supplier) {
        andOr = false;
        return join(supplier.get());
    }

    public LambdaFilter join(Filter filter) {
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
