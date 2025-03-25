
package com.github.easyjpa;

/**
 * 
 * @Description: JpaPageImpl
 * @Author: Fred Feng
 * @Date: 18/10/2024
 * @Version 1.0.0
 */
public class JpaPageImpl<E, T> implements JpaPage<E, T> {

    private final JpaQuery<E, T> query;
    private final JpaQuery<E, Long> counter;
    private final JpaCustomQuery<?> customQuery;

    JpaPageImpl(JpaQuery<E, T> query, JpaQuery<E, Long> counter, JpaCustomQuery<?> customQuery) {
        this.query = query;
        this.counter = counter;
        this.customQuery = customQuery;
    }

    @Override
    public JpaPage<E, T> filter(Filter filter) {
        query.filter(filter);
        counter.filter(filter);
        return this;
    }

    @Override
    public JpaPage<E, T> sort(JpaSort... sorts) {
        query.sort(sorts);
        return this;
    }

    @Override
    public JpaPageGroupBy<E, T> groupBy(FieldList fieldList) {
        JpaGroupBy<E, T> groupQuery = query.groupBy(fieldList);
        JpaGroupBy<E, Long> counterQuery = counter.groupBy(fieldList);
        return new JpaPageGroupByImpl<E, T>(groupQuery, counterQuery, customQuery);
    }


    @Override
    public <X> JpaSubQuery<X, X> subQuery(Class<X> entityClass, String alias) {
        return query.subQuery(entityClass, alias);
    }

    @Override
    public <X, Y> JpaSubQuery<X, Y> subQuery(Class<X> entityClass, String alias,
            Class<Y> resultClass) {
        return query.subQuery(entityClass, alias, resultClass);
    }

    @Override
    public JpaPageResultSet<T> selectThis() {
        query.selectThis();
        return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(),
                customQuery);
    }

    @Override
    public JpaPageResultSet<T> selectAlias(String... tableAliases) {
        query.selectAlias(tableAliases);
        return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(),
                customQuery);
    }

    @Override
    public JpaPageResultSet<T> select(ColumnList columnList) {
        query.select(columnList);
        return new JpaPageResultSetImpl<T>(query.model(), query.query(), counter.query(),
                customQuery);
    }

    @Override
    public <X> JpaPage<X, T> join(Class<X> joinClass, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.join(joinClass, alias, on);
        JpaQuery<X, Long> joinCounter = counter.join(joinClass, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> leftJoin(Class<X> joinClass, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.leftJoin(joinClass, alias, on);
        JpaQuery<X, Long> joinCounter = counter.leftJoin(joinClass, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> rightJoin(Class<X> joinClass, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.rightJoin(joinClass, alias, on);
        JpaQuery<X, Long> joinCounter = counter.rightJoin(joinClass, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> join(String attributeName, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.join(attributeName, alias, on);
        JpaQuery<X, Long> joinCounter = counter.join(attributeName, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> leftJoin(String attributeName, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.leftJoin(attributeName, alias, on);
        JpaQuery<X, Long> joinCounter = counter.leftJoin(attributeName, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> rightJoin(String attributeName, String alias, Filter on) {
        JpaQuery<X, T> joinQuery = query.rightJoin(attributeName, alias, on);
        JpaQuery<X, Long> joinCounter = counter.rightJoin(attributeName, alias, on);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

    @Override
    public <X> JpaPage<X, T> crossJoin(Class<X> joinClass, String alias) {
        JpaQuery<X, T> joinQuery = query.crossJoin(joinClass, alias);
        JpaQuery<X, Long> joinCounter = counter.crossJoin(joinClass, alias);
        return new JpaPageImpl<X, T>(joinQuery, joinCounter, customQuery);
    }

}
