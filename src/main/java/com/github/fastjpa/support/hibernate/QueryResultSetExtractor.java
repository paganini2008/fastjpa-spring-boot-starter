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
package com.github.fastjpa.support.hibernate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

/**
 * 
 * @Description: QueryResultSetExtractor
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public interface QueryResultSetExtractor<T> {

    List<T> extractData(Session session, NativeQuery<?> query);

}
