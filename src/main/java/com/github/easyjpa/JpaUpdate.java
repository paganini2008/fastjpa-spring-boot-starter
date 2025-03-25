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

    default JpaUpdate<E> set(SerializedFunction<E, ?> function, Object value) {
        LambdaInfo lambdaInfo = LambdaUtils.inspect(function);
        return set(lambdaInfo.getAttributeName(), value);
    }

    default JpaUpdate<E> set(SerializedFunction<E, ?> function1, Object value1,
            SerializedFunction<E, ?> function2, Object value2) {
        LambdaInfo lambdaInfo1 = LambdaUtils.inspect(function1);
        LambdaInfo lambdaInfo2 = LambdaUtils.inspect(function2);
        return set(lambdaInfo1.getAttributeName(), value1).set(lambdaInfo2.getAttributeName(),
                value2);
    }

    default JpaUpdate<E> set(SerializedFunction<E, ?> function1, Object value1,
            SerializedFunction<E, ?> function2, Object value2, SerializedFunction<E, ?> function3,
            Object value3) {
        LambdaInfo lambdaInfo1 = LambdaUtils.inspect(function1);
        LambdaInfo lambdaInfo2 = LambdaUtils.inspect(function2);
        LambdaInfo lambdaInfo3 = LambdaUtils.inspect(function3);
        return set(lambdaInfo1.getAttributeName(), value1)
                .set(lambdaInfo2.getAttributeName(), value2)
                .set(lambdaInfo3.getAttributeName(), value3);
    }

    <T> JpaUpdate<E> set(String attributeName, T value);

    <T> JpaUpdate<E> set(String attributeName1, T value1, String attributeName2, T value2);

    <T> JpaUpdate<E> set(String attributeName1, T value1, String attributeName2, T value2,
            String attributeName3, T value3);

    <T> JpaUpdate<E> set(String attributeName, String anotherAttributeName);

    default <T> JpaUpdate<E> setField(SerializedFunction<E, T> function, Field<T> value) {
        LambdaInfo lambdaInfo = LambdaUtils.inspect(function);
        return setField(lambdaInfo.getAttributeName(), value);
    }

    <T> JpaUpdate<E> setField(String attributeName, Field<T> value);

    <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass);

    <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass);
}
