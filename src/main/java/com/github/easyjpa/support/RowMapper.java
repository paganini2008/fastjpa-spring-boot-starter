
package com.github.easyjpa.support;

import java.util.Map;

/**
 * 
 * @Description: RowMapper
 * @Author: Fred Feng
 * @Date: 18/08/2021
 * @Version 1.0.0
 */
@FunctionalInterface
public interface RowMapper<T> {

    T mapRow(int index, Map<String, Object> map);

}
