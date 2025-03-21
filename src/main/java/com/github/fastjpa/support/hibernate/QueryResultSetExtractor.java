
package com.github.fastjpa.support.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

/**
 * 
 * @Description: QueryResultSetExtractor
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface QueryResultSetExtractor<T> {

    List<T> extractData(Session session, NativeQuery<?> query);

}
