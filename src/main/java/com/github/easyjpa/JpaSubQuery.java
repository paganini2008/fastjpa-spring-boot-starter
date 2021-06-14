package com.github.easyjpa;

import com.github.easyjpa.LambdaUtils.LambdaInfo;

/**
 * 
 * @Description: JpaSubQuery
 * @Author: Fred Feng
 * @Date: 18/10/2024
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

    JpaSubQuery<X, Y> select(String alias, String attributeName);

    default JpaSubQuery<X, Y> select(SerializedFunction<X, Y> function) {
        return select(Property.forName(function));
    }

    JpaSubQuery<X, Y> select(Field<Y> field);

    JpaSubQuery<X, Y> distinct();

    <Z> JpaSubQuery<Z, Y> join(String attributeName, String alias, Filter on);

    <Z> JpaSubQuery<Z, Y> leftJoin(String attributeName, String alias, Filter on);

    <Z> JpaSubQuery<Z, Y> rightJoin(String attributeName, String alias, Filter on);

    default <Z> JpaSubQuery<Z, Y> join(SerializedFunction<Z, ?> function, String alias, Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return join(info.getAttributeName(), alias, on);
    }

    default <Z> JpaSubQuery<Z, Y> leftJoin(SerializedFunction<Z, ?> function, String alias,
            Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return leftJoin(info.getAttributeName(), alias, on);
    }

    default <Z> JpaSubQuery<Z, Y> rightJoin(SerializedFunction<Z, ?> function, String alias,
            Filter on) {
        LambdaInfo info = LambdaUtils.inspect(function);
        TableAlias.put(info.getAttributeType(), alias);
        return rightJoin(info.getAttributeName(), alias, on);
    }

}
