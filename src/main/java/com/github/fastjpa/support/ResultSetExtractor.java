
package com.github.fastjpa.support;

import jakarta.persistence.Query;

/**
 * 
 * @Description: ResultSetExtractor
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface ResultSetExtractor<T> {

    T extractData(Query query);

}
