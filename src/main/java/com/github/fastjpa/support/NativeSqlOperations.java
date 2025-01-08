/**
 * Copyright 2017-2025 Fred Feng (paganini.fy@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.fastjpa.support;

import java.util.Map;
import com.github.paganini2008.devtools.collection.CaseInsensitiveMap;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * @Description: NativeSqlOperations
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public interface NativeSqlOperations<E> {

    ResultSetSlice<E> select(String sql, Object[] arguments);

    <T> T getSingleResult(String sql, Object[] arguments, Class<T> requiredType);

    <T> ResultSetSlice<T> select(String sql, Object[] arguments, Class<T> resultClass);

    <T> ResultSetSlice<T> select(String sql, Object[] arguments, RowMapper<T> rowMapper);

    default ResultSetSlice<Map<String, Object>> selectForMap(String sql, Object[] arguments) {
        return select(sql, arguments, (index, data) -> new CaseInsensitiveMap<>(data));
    }

    <T> T execute(String sql, Object[] arguments, ResultSetExtractor<T> extractor);

    int executeUpdate(String sql, Object[] arguments);

}
