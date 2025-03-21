package com.github.fastjpa.page;

import org.springframework.data.domain.Pageable;
import com.github.fastjpa.ListableQuery;

/**
 * 
 * @Description: Paginator
 * @Author: Fred Feng
 * @Date: 08/10/2024
 * @Version 1.0.0
 */
public interface PageableQuery<T> extends ListableQuery<T>, Countable {

    default PageResponse<T> list(Pageable pageable) {
        return new PageResponseImpl<T>(pageable, this);
    }
}
