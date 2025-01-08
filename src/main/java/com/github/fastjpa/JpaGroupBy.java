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
package com.github.fastjpa;

import javax.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaGroupBy
 * @Author: Fred Feng
 * @Date: 08/01/2025
 * @Version 1.0.0
 */
public interface JpaGroupBy<E, T> {

    JpaGroupBy<E, T> having(Filter filter);

    default JpaQueryResultSet<T> select(String... attributeNames) {
        return select(new ColumnList().addColumns(attributeNames));
    }

    default JpaQueryResultSet<T> select(String alias, String[] attributeNames) {
        return select(new ColumnList().addColumns(alias, attributeNames));
    }

    default JpaQueryResultSet<T> select(Column... columns) {
        return select(new ColumnList(columns));
    }

    default JpaQueryResultSet<T> select(Field<?>... fields) {
        return select(new ColumnList().addColumns(fields));
    }

    JpaQueryResultSet<T> select(ColumnList columnList);

    JpaGroupBy<E, T> sort(JpaSort... sorts);

    CriteriaQuery<T> query();

    Model<E> model();

}
