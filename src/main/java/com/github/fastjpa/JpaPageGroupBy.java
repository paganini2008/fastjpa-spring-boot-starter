
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaPageGroupBy
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaPageGroupBy<E, T> {

    JpaPageGroupBy<E, T> having(Filter expression);

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

    JpaPageGroupBy<E, T> sort(JpaSort... sorts);

    CriteriaQuery<T> query();

    Model<E> model();

}
