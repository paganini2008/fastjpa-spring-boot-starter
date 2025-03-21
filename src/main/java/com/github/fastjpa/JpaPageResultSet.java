
package com.github.fastjpa;

import com.github.fastjpa.page.Countable;
import com.github.fastjpa.page.PageableQuery;

/**
 * 
 * @Description: JpaPageResultSet
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaPageResultSet<T> extends ListableQuery<T>, Countable {

    <R> PageableQuery<R> setTransformer(Transformer<T, R> transformer);

}
