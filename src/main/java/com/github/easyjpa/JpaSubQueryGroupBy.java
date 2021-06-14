
package com.github.easyjpa;

/**
 * 
 * @Description: JpaSubQueryGroupBy
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
public interface JpaSubQueryGroupBy<E, T> {

    JpaSubQueryGroupBy<E, T> having(Filter filter);

    default JpaSubQueryGroupBy<E, T> select(String attributeName) {
        return select(null, attributeName);
    }

    JpaSubQueryGroupBy<E, T> select(String alias, String attributeName);

    JpaSubQueryGroupBy<E, T> select(Field<T> field);

    default JpaSubQueryGroupBy<E, T> select(SerializedFunction<E, T> function) {
        return select(Property.forName(function));
    }

}
