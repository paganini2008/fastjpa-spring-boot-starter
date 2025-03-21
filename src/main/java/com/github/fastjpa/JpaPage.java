
package com.github.fastjpa;

/**
 * 
 * @Description: JpaPage
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaPage<E, T> {

    JpaPage<E, T> filter(Filter filter);

    JpaPage<E, T> sort(JpaSort... sorts);

    JpaPageResultSet<T> selectThis();

    JpaPageResultSet<T> selectAlias(String... tableAliases);

    JpaPageResultSet<T> select(ColumnList columnList);

    default JpaPageGroupBy<E, T> groupBy(String... attributeNames) {
        return groupBy(new FieldList(attributeNames));
    }

    default JpaPageGroupBy<E, T> groupBy(String alias, String[] attributeNames) {
        return groupBy(new FieldList(alias, attributeNames));
    }

    default JpaPageGroupBy<E, T> groupBy(Field<?>... fields) {
        return groupBy(new FieldList(fields));
    }

    JpaPageGroupBy<E, T> groupBy(FieldList fieldList);

    default <X> JpaPage<X, T> join(String attributeName, String alias) {
        return join(attributeName, alias, null);
    }

    default <X> JpaPage<X, T> leftJoin(String attributeName, String alias) {
        return leftJoin(attributeName, alias, null);
    }

    default <X> JpaPage<X, T> rightJoin(String attributeName, String alias) {
        return rightJoin(attributeName, alias, null);
    }

    <X> JpaPage<X, T> join(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> leftJoin(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> rightJoin(String attributeName, String alias, Filter on);

}
