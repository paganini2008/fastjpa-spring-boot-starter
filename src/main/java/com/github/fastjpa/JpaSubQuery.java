package com.github.fastjpa;

/**
 * 
 * @Description: JpaSubQuery
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaSubQuery<X, Y> extends SubQueryBuilder<Y> {

    JpaSubQuery<X, Y> filter(Filter filter);

    default JpaSubQueryGroupBy<X, Y> groupBy(String alias, String... attributeNames) {
        return groupBy(new FieldList(alias, attributeNames));
    }

    default JpaSubQueryGroupBy<X, Y> groupBy(Field<?>... fields) {
        return groupBy(new FieldList(fields));
    }

    JpaSubQueryGroupBy<X, Y> groupBy(FieldList fieldList);

    default JpaSubQuery<X, Y> select(String attributeName) {
        return select(null, attributeName);
    }

    JpaSubQuery<X, Y> select(String alias, String attributeName);

    JpaSubQuery<X, Y> select(Field<Y> field);

    JpaSubQuery<X, Y> distinct(boolean distinct);

    default <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias) {
        return join(attributeName, alias, null);
    }

    default <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias) {
        return leftJoin(attributeName, alias, null);
    }

    default <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias) {
        return rightJoin(attributeName, alias, null);
    }

    <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias, Filter on);

    <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias, Filter on);

    <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias, Filter on);

}
