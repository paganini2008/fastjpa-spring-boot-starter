package com.github.fastjpa;

import java.util.List;
import com.github.fastjpa.LambdaUtils.LambdaInfo;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaQuery
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public interface JpaQuery<E, T> {

    default <R> JpaQuery<E, T> eq(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.eq(sf, value));
    }

    default JpaQuery<E, T> eqProperty(SerializedFunction<E, ?> leftSf,
            SerializedFunction<E, ?> rightSf) {
        return filter(Restrictions.eqProperty(leftSf, rightSf));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> lt(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.lt(sf, value));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> lte(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.lte(sf, value));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> gt(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.gt(sf, value));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> gte(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.gte(sf, value));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> ne(SerializedFunction<E, R> sf, R value) {
        return filter(Restrictions.ne(sf, value));
    }

    default JpaQuery<E, T> neProperty(SerializedFunction<E, ?> leftSf,
            SerializedFunction<E, ?> rightSf) {
        return filter(Restrictions.neProperty(leftSf, rightSf));
    }

    default JpaQuery<E, T> like(SerializedFunction<E, String> sf, String pattern) {
        return filter(Restrictions.like(sf, pattern));
    }

    default JpaQuery<E, T> like(SerializedFunction<E, String> sf, String pattern, char escapeChar) {
        return filter(Restrictions.like(sf, pattern, escapeChar));
    }

    default JpaQuery<E, T> notLike(SerializedFunction<E, String> sf, String pattern) {
        return filter(Restrictions.notLike(sf, pattern));
    }

    default JpaQuery<E, T> notLike(SerializedFunction<E, String> sf, String pattern,
            char escapeChar) {
        return filter(Restrictions.notLike(sf, pattern, escapeChar));
    }

    default <R> JpaQuery<E, T> in(SerializedFunction<E, R> sf, Iterable<R> iterable) {
        return filter(Restrictions.in(sf, iterable));
    }

    default <R extends Comparable<R>> JpaQuery<E, T> between(SerializedFunction<E, R> sf,
            R startValue, R endValue) {
        return filter(Restrictions.between(sf, startValue, endValue));
    }

    default JpaQuery<E, T> isNull(SerializedFunction<E, String> sf) {
        return filter(Restrictions.isNull(sf));
    }

    default JpaQuery<E, T> notNull(SerializedFunction<E, String> sf) {
        return filter(Restrictions.notNull(sf));
    }

    default JpaQuery<E, T> and(Filter... filters) {
        return filter(Restrictions.and(List.of(filters)));
    }

    default JpaQuery<E, T> or(Filter... filters) {
        return filter(Restrictions.or(List.of(filters)));
    }

    JpaQuery<E, T> filter(Filter filter);

    default JpaQuery<E, T> orderBy(SerializedFunction<E, String> sf, boolean asc) {
        return sort(asc ? JpaSort.asc(sf) : JpaSort.desc(sf));
    }

    JpaQuery<E, T> sort(JpaSort... sorts);

    default JpaGroupBy<E, T> groupBy(String... attributeNames) {
        return groupBy(new FieldList().addFields(attributeNames));
    }

    default JpaGroupBy<E, T> groupBy(String alias, String[] attributeNames) {
        return groupBy(new FieldList().addFields(alias, attributeNames));
    }

    default JpaGroupBy<E, T> groupBy(Field<?>... fields) {
        return groupBy(new FieldList(fields));
    }

    default JpaGroupBy<E, T> groupBy(
            @SuppressWarnings("unchecked") SerializedFunction<E, ?>... sFuns) {
        return groupBy(new FieldList().addField(sFuns));
    }

    JpaGroupBy<E, T> groupBy(FieldList fieldList);

    JpaQueryResultSet<T> selectThis();

    JpaQueryResultSet<T> selectAlias(String... tableAliases);

    default JpaQueryResultSet<T> select(String... attributeNames) {
        return select(new ColumnList().addColumns(attributeNames));
    }

    default JpaQueryResultSet<T> select(String alias, String[] attributeNames) {
        return select(new ColumnList().addColumns(alias, attributeNames));
    }

    default JpaQueryResultSet<T> select(Column... columns) {
        return select(new ColumnList(columns));
    }

    default JpaQueryResultSet<T> select(Field<?>... fields) {
        return select(new ColumnList().addColumns(fields));
    }

    JpaQueryResultSet<T> select(ColumnList columnList);

    default T one(String attributeName) {
        return one(Column.forName(attributeName));
    }

    default T one(String alias, String attributeName) {
        return one(Column.forName(alias, attributeName));
    }

    default T one(Field<T> field) {
        return one(field.as(field.toString()));
    }

    T one(Column column);

    JpaQuery<E, T> distinct(boolean distinct);

    <X> JpaQuery<X, T> join(String attributeName, String alias, Filter on);

    <X> JpaQuery<X, T> leftJoin(String attributeName, String alias, Filter on);

    <X> JpaQuery<X, T> rightJoin(String attributeName, String alias, Filter on);

    default <X> JpaQuery<X, T> join(SerializedFunction<X, T> sf, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(sf);
        return join(info.getAttributeName(), alias, on);
    }

    default <X> JpaQuery<X, T> leftJoin(SerializedFunction<X, T> sf, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(sf);
        return leftJoin(info.getAttributeName(), alias, on);
    }

    default <X> JpaQuery<X, T> rightJoin(SerializedFunction<X, T> sf, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(sf);
        return rightJoin(info.getAttributeName(), alias, on);
    }

    <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass, String alias);

    <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, String alias, Class<Y> resultClass);

    CriteriaQuery<T> query();

    Model<E> model();

}
