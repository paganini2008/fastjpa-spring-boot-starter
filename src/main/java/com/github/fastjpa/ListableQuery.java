package com.github.fastjpa;

import java.util.List;

/**
 * 
 * @Description: ListableQuery
 * @Author: Fred Feng
 * @Date: 20/03/2025
 * @Version 1.0.0
 */
public interface ListableQuery<T> {

    default List<T> list() {
        return list(-1);
    }

    default List<T> list(int maxResults) {
        return list(maxResults, 0);
    }

    default T first() {
        List<T> list = list(1);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    List<T> list(int maxResults, long firstResult);
}
