
package com.github.fastjpa;

import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * @Description: JpaPageResultSet
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaPageResultSet<T> extends ResultSetSlice<T> {

    <R> ResultSetSlice<R> setTransformer(Transformer<T, R> transformer);

}
