/**
 * Copyright 2017-2021 Fred Feng (paganini.fy@gmail.com)
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

/**
 * 
 * @Description: JpaPage
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaPage<E, T> {

    JpaPage<E, T> filter(Filter filter);

    JpaPage<E, T> sort(JpaSort... sorts);

    JpaPageResultSet<T> selectThis();

    JpaPageResultSet<T> selectAlias(String... tableAliases);

    default JpaPageResultSet<T> select(String... attributeNames) {
        return select(new ColumnList().addColumns(attributeNames));
    }

    default JpaPageResultSet<T> select(String alias, String[] attributeNames) {
        return select(new ColumnList().addColumns(alias, attributeNames));
    }

    default JpaPageResultSet<T> select(Column... columns) {
        return select(new ColumnList(columns));
    }

    default JpaPageResultSet<T> select(Field<?>... fields) {
        return select(new ColumnList().addColumns(fields));
    }

    JpaPageResultSet<T> select(ColumnList columnList);

    default JpaPageGroupBy<E, T> groupBy(String... attributeNames) {
        return groupBy(new FieldList().addFields(attributeNames));
    }

    default JpaPageGroupBy<E, T> groupBy(String alias, String[] attributeNames) {
        return groupBy(new FieldList().addFields(alias, attributeNames));
    }

    default JpaPageGroupBy<E, T> groupBy(Field<?>... fields) {
        return groupBy(new FieldList(fields));
    }

    JpaPageGroupBy<E, T> groupBy(FieldList fieldList);

    default <X> JpaPage<X, T> join(String attributeName, String alias) {
        return join(attributeName, alias, null);
    }

    default <X> JpaPage<X, T> leftJoin(String attributeName, String alias) {
        return leftJoin(attributeName, alias, null);
    }

    default <X> JpaPage<X, T> rightJoin(String attributeName, String alias) {
        return rightJoin(attributeName, alias, null);
    }

    <X> JpaPage<X, T> join(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> leftJoin(String attributeName, String alias, Filter on);

    <X> JpaPage<X, T> rightJoin(String attributeName, String alias, Filter on);

}
