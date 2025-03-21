package com.github.fastjpa.page;

import java.util.List;

/**
 * @Description: EachPage
 * @Author: Fred Feng
 * @Date: 08/03/2023
 * @Version 1.0.0
 */
public interface EachPage<T> {

    boolean isEmpty();

    boolean isLastPage();

    boolean isFirstPage();

    boolean hasNextPage();

    boolean hasPreviousPage();

    int getTotalPages();

    long getTotalRecords();

    long getOffset();

    int getPageSize();

    int getPageNumber();

    List<T> getContent();
}
