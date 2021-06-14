
package com.github.easyjpa;

import com.github.easyjpa.LambdaUtils.LambdaInfo;

/**
 * 
 * @Description: JpaPage
 * @Author: Fred Feng
 * @Date: 18/10/2024
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

    default JpaPageGroupBy<E, T> groupBy(SerializedFunction<E, ?> function) {
        return groupBy(new FieldList(function));
    }

    JpaPageGroupBy<E, T> groupBy(FieldList fieldList);

    default <X> JpaPage<E, T> join(SerializedFunction<X, ?> function, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return join(info.getAttributeName(), alias, on);
    }

    default <X> JpaPage<X, T> leftJoin(SerializedFunction<X, ?> function, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return leftJoin(info.getAttributeName(), alias, on);
    }

    default <X> JpaPage<E, T> rightJoin(SerializedFunction<X, ?> function, String alias,
            Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return rightJoin(info.getAttributeName(), alias, on);
    }

    <X> JpaPage<X, T> join(Class<X> joinClass, String alias, Filter on);

    <X> JpaPage<X, T> join(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> leftJoin(Class<X> joinClass, String alias, Filter on);

    <X> JpaPage<X, T> leftJoin(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> rightJoin(Class<X> joinClass, String alias, Filter on);

    <X> JpaPage<X, T> rightJoin(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> crossJoin(Class<X> joinClass, String alias);

}
