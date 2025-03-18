
package com.github.fastjpa;

/**
 * 
 * @Description: JpaCustomUpdate
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaCustomUpdate<X> {

    JpaUpdate<X> update(Class<X> entityClass);

    JpaDelete<X> delete(Class<X> entityClass);

    int executeUpdate(JpaDeleteCallback<X> callback);

    int executeUpdate(JpaUpdateCallback<X> callback);
}
