
package com.github.fastjpa;

import jakarta.persistence.criteria.CriteriaQuery;

/**
 * 
 * @Description: JpaGroupBy
 * @Author: Fred Feng
 * @Date: 18/03/2025
 * @Version 1.0.0
 */
public interface JpaGroupBy<E, T> {

    JpaGroupBy<E, T> having(Filter filter);

    JpaQueryResultSet<T> select(ColumnList columnList);

    default JpaGroupBy<E, T> orderBy(int number, boolean asc) {
        return sort(asc ? JpaSort.asc(Fields.toInteger(number))
                : JpaSort.desc(Fields.toInteger(number)));
    }

    JpaGroupBy<E, T> sort(JpaSort... sorts);

    CriteriaQuery<T> query();

    Model<E> model();

}
