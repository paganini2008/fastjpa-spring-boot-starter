package com.github.fastjpa;

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

    JpaQuery<E, T> filter(Filter filter);

    default JpaQuery<E, T> orderBy(SerializedFunction<E, ?> sf, boolean asc) {
        return sort(asc ? JpaSort.asc(sf) : JpaSort.desc(sf));
    }

    JpaQuery<E, T> sort(JpaSort... sorts);

    default JpaGroupBy<E, T> groupBy(String... attributeNames) {
        return groupBy(new FieldList(attributeNames));
    }

    default JpaGroupBy<E, T> groupBy(String alias, String[] attributeNames) {
        return groupBy(new FieldList(alias, attributeNames));
    }

    default JpaGroupBy<E, T> groupBy(Field<?>... fields) {
        return groupBy(new FieldList(fields));
    }

    JpaGroupBy<E, T> groupBy(FieldList fieldList);

    JpaQueryResultSet<T> selectThis();

    JpaQueryResultSet<T> selectAlias(String... tableAliases);

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
