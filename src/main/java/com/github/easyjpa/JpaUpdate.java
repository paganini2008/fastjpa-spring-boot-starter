package com.github.easyjpa;

import com.github.easyjpa.LambdaUtils.LambdaInfo;

/**
 * 
 * JpaUpdate represents a update statement
 * 
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public interface JpaUpdate<E> extends Executable {

    JpaUpdate<E> filter(Filter filter);

    default <T> JpaUpdate<E> set(SerializedFunction<E, T> function, T value) {
        LambdaInfo lambdaInfo = LambdaUtils.inspect(function);
        return set(lambdaInfo.getAttributeName(), value);
    }

    <T> JpaUpdate<E> set(String attributeName, T value);

    <T> JpaUpdate<E> set(String attributeName, String anotherAttributeName);

    default <T> JpaUpdate<E> setField(SerializedFunction<E, T> function, Field<T> value) {
        LambdaInfo lambdaInfo = LambdaUtils.inspect(function);
        return setField(lambdaInfo.getAttributeName(), value);
    }

    <T> JpaUpdate<E> setField(String attributeName, Field<T> value);

    <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass);

    <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass);
}
