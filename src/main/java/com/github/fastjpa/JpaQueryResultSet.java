
package com.github.fastjpa;

import com.github.fastjpa.page.Countable;

/**
 * 
 * @Description: JpaQueryResultSet
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaQueryResultSet<T> extends ListableQuery<T>, Countable {

    <R> ListableQuery<R> setTransformer(Transformer<T, R> transformer);

}
