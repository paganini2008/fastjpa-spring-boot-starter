
package com.github.fastjpa;

import java.util.List;

/**
 * 
 * @Description: JpaCustomQuery
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaCustomQuery<X> {

    <T> T getSingleResult(JpaQueryCallback<T> callback);

    <T> List<T> getResultList(JpaQueryCallback<T> callback);

    <T> List<T> getResultList(JpaQueryCallback<T> callback, int maxResults, long firstResult);
}
