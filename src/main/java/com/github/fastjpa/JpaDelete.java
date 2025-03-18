
package com.github.fastjpa;

/**
 * 
 * @Description: JpaDelete
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaDelete<E> extends Executable {

    JpaDelete<E> filter(Filter filter);

    <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass);

    <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, Class<Y> resultClass);

}
